const BadgeType = {
    POPULAR_QUESTION: 'POPULAR_QUESTION',
    NOTABLE_QUESTION: 'NOTABLE_QUESTION',
    FAMOUS_QUESTION: 'FAMOUS_QUESTION',

    ANNOUNCER: 'ANNOUNCER',
    BOOSTER: 'BOOSTER',
    PUBLICIST: 'PUBLICIST',

    isUserInvolved: function (type) {
        return type === this.ANNOUNCER || type === this.BOOSTER || type === this.PUBLICIST
    },
    isValid: function (type) {
        for (const key of Object.keys(this)) {
            if (typeof this[key] !== 'function' && this[key] === type)
                return true
        }
    }
}

module.exports = BadgeType;
