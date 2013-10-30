Exceptional design for the Android application cloud backend

### Requirements
 1. Registering a (deviceId, userEmail) pair with the server which will return an authentication token
 2. Fetching the contents of the alphas
 3. Fetching the group memberships of a user
 4. Fetching the progress for a specific group
 5. Uploading check-box modifications for synchronization
 6. Fetching the comments for a particular group,alpha
 7. Uploading comments for synchronization


### Draft of API contracts
For all endpoints, request and response headers will include the following
```
Content-Type: application/json
```
When authentication is required, also include the auth token:
```
Authorization: Devise <token>
```

### Registration
```
Method: POST /registration
Request Body: {
    deviceId: <guid>, 
    userEmail: "bob.singh@gmail.com"
}
Response Body: {
    token: <token>
}
```

### Fetch the contents of the alphas
```
Method: GET /alphas
Method: GET /alphas/omg1.0
Response Body: {
    version: "OMG 1.0"
    alphas: [
        {
            alphaId: <guid>,
            alphaName: "Some Title Here",
            color: "Blue",
            concern: "Customer",
            states: [
                {
                    stateId: <guid>,
                    stateName: "Card Name Here",
                    checklistItems: [
                        {
                            checklistItemId: <guid>,
                            ckecklistItemName: "Some label here"
                        },
                        /*some other checkboxes here*/
                    ]
                },
                /*some other cards here*/
            ]
        },
        /*some other alphas here*/
    ]
}
```

### Fetch the group memberships
```
Method: GET /groups
Response Body: {
    groups: [
        {
            groupId: <guid>,
            groupName: "NotSoExceptional"
        },
        {
            groupId: <guid>,
            groupName: "The Foobar Awesome Conglomerate"
        }
    ]
}
```

### Fetch the progress for a specific group
*Not quite sure if this is really the way to do it here.*
```
Method: GET /progress/<groupId>
Response Body: {
    currentVersion: 2,
    checkboxes: [<guid>]
}
```


### Upload the checkbox modifications
```
Method: POST /progress/<groupId>
Request Body: {
    currentVersion: 2,
    checkboxes: [<guid>, <guid>]
}
Response Body: {
    forceUpdate: true
}
```

### Upload comments for synchronization
```
Method: POST /comments/<groupId>/<cardId>
Request Body: {
    alphaId: <guid>,
    comment: "The professor says that we don't have enough exceptions in our code"
}
Response Body: {
    forceUpdate: false
}
```





