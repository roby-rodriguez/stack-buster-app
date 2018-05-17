var StackBusterAPI = {};

StackBusterAPI.BadgeType = Object.create(null);

StackBusterAPI.BadgeType.POPULAR_QUESTION = 'POPULAR_QUESTION';
StackBusterAPI.BadgeType.NOTABLE_QUESTION = 'NOTABLE_QUESTION';
StackBusterAPI.BadgeType.FAMOUS_QUESTION = 'FAMOUS_QUESTION';

StackBusterAPI.BadgeType.ANNOUNCER = 'ANNOUNCER';
StackBusterAPI.BadgeType.BOOSTER = 'BOOSTER';
StackBusterAPI.BadgeType.PUBLICIST = 'PUBLICIST';

StackBusterAPI.BadgeType.isUserInvolved = function (type) {
    return type === this.ANNOUNCER || type === this.BOOSTER || type === this.PUBLICIST
};
StackBusterAPI.BadgeType.isValid = function (type) {
    for (var key in this) {
        // avoided need for extra `this.hasOwnProperty(key)` check
        if (typeof this[key] !== 'function' && this[key] === type)
            return true
    }
};

StackBusterAPI.Urls = {
    QUESTION: function (qid) {
        return 'https://api.stackexchange.com/2.2/questions/' + qid + '?order=desc&sort=activity&site=stackoverflow'
    },
    USER: function (uid) {
        return 'https://api.stackexchange.com/2.2/users/' + uid + '?order=asc&sort=reputation&site=stackoverflow'
    }
};

StackBusterAPI.enqueue = function (qid, uid, badgeType) {

};
