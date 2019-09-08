
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

let requestId;

fetch('/v1/registrations/start',
        {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName: 'ibrahimin2004@gmail.com', displayName: 'ibhi'  })
         }
    ).then(function(res) {
        return res.json();
    })
    .then((res) => {
        console.log("This is data ", res);
        requestId = res.requestId;
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
    .catch(function(err) { console.error(err) });