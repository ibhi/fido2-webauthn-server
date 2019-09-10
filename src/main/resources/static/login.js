'use strict';

(() => {

    $('.toast').toast();

    let requestId;

    /**
     * Converts PublicKeyCredential into serialised JSON
     * @param  {Object} pubKeyCred
     * @return {Object}            - JSON encoded publicKeyCredential
     */
    let publicKeyCredentialToJSON = (pubKeyCred) => {
        if(pubKeyCred instanceof Array) {
            let arr = [];
            for(let i of pubKeyCred)
                arr.push(publicKeyCredentialToJSON(i));

            return arr
        }

        if(pubKeyCred instanceof ArrayBuffer) {
            return base64url.encode(pubKeyCred)
        }

        if(pubKeyCred instanceof Object) {
            let obj = {};

            for (let key in pubKeyCred) {
                obj[key] = publicKeyCredentialToJSON(pubKeyCred[key])
            }

            return obj
        }

        return pubKeyCred
    }

    let preformatGetAssertReq = (getAssert) => {
        getAssert.publicKey.challenge = base64url.decode(getAssert.publicKey.challenge);

        for(let allowCred of getAssert.publicKey.allowCredentials) {
            allowCred.id = base64url.decode(allowCred.id);
        }

        return getAssert
    }

    let sendWebAuthnResponse = (body) => {
        return fetch('/v1/authentication/finish', {
            method: 'POST',
            credentials: 'include',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        })
        .then((response) => {
            if(!response.ok) {
                throw new Error(`Server responded with error. The message is: ${response.message}`);
            }
            return response.json()
        })
        .then((res) => console.log(res));
    }

    $('#register-btn').click(() => {
        let userName = $('#email').val();

        fetch('/v1/authentication/start',
                {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userName: userName })
                 }
            ).then(function(res) {
                if(!res.ok) {
                    throw new Error(`Server responded with error. The message is: ${res.message}`);
                }
                return res.json();
            })
            .then((res) => {
                console.log("This is authentication start response from server ", res);
                requestId = res.requestId;
                console.log("RequestId: ", requestId);
                res = preformatGetAssertReq(res)
                console.log("Challenge", res.publicKey.challenge);
                let result = navigator.credentials.get(res);
                console.log("Result from get: ", result);
                return result;
            })
            .then((response) => {
                let getAssertionResponse = publicKeyCredentialToJSON(response);
                return sendWebAuthnResponse( { requestId: requestId, userName: userName, publicKeyCredential: getAssertionResponse });
            })
            .then(() => alert("Logged in successfully"))
            .catch(() => alert("Something went wrong!"));

    });

})();
