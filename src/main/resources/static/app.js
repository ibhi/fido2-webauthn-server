'use strict';

const USER_NAME = 'ibrahimin2004@gmail.com'

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
    .then((response) => response.json())
    .then((response) => {
        if(response.status !== 'ok')
            throw new Error(`Server responded with error. The message is: ${response.message}`);

        return response
    })
}

let startAuthentication = () => {
    return fetch('/v1/authentication/start',
            {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userName: USER_NAME })
             }
        ).then(function(res) {
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
            return sendWebAuthnResponse( { requestId: requestId, userName: USER_NAME, publicKeyCredential: getAssertionResponse })
        });
}



fetch('/v1/registrations/start',
        {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName: USER_NAME, displayName: 'ibhi'  })
         }
    ).then(function(res) {
        return res.json();
    })
    .then((res) => {
        console.log("This is data ", res);
        requestId = res.requestId;
        console.log("RequestId: ", requestId);
        res.publicKey.challenge = base64url.decode(res.publicKey.challenge);
        res.publicKey.user.id = base64url.decode(res.publicKey.user.id);
        console.log("Challenge", res.publicKey.challenge);
        return navigator.credentials.create(res);
    })
    .then((pubKeyCreds) => {
        console.log(pubKeyCreds);
        let makeCredResponse = publicKeyCredentialToJSON(pubKeyCreds);
        console.log(makeCredResponse);
        return fetch('/v1/registrations/finish', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ requestId: requestId, publicKeyCredential: makeCredResponse })
        })
    })
    .then((res) => res.json())
    .then(data => console.log(data))
    .then(startAuthentication)
    .catch(function(err) { console.error(err) });



