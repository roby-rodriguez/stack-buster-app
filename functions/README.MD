Deployed by running `firebase deploy --only functions` in terminal. This will deploy `index.js`, but if you want to deploy a different source this can be configured.

Not really *entirely* useful due to the following reasons: 
* free plan does not allow external HTTP calls from within Firebase functions
* again, if there's no billing information registered quota limits are rather low

As a result, a better idea would be to move these functions-validations toward [firebase rules](https://firebase.google.com/docs/database/security/#section-validation). Check out `~\database-rules\rules.json` also.

A possible combination would then be:

Functions | Rules | Description
--- | --- | ---
index.js | - | validations covered entirely by functions
index.lite.js | rules.lite.json | validations split between functions and rules (and maybe additional backend -> see scheduler)
index.ultra.lite.js | rules.json | validations covered by rules (extras handled by scheduler)
