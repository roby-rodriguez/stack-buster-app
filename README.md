# StackBuster App

### Client

TODO some words about it

### Firebase Rules

This project uses a lite version of a ruleset. Read more [here](functions/README.MD).

### Scheduler

TODO some words about it + configs

### Deployment

To deploy the project you should follow these steps:
1) Run `firebase login:ci`
2) If successful, copy the received token to a file *token.key* in the project root directory
3) Go to **functions**/functions and run `npm install`
4) Go to **scheduler** and run `mvn deploy`
