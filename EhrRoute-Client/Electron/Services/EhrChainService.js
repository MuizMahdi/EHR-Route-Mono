const ehrDbDAO = require('../DAOs/EhrChainDAO');

module.exports = {

    initializeNodeEhrChain: function() {
        ehrDbDAO.initializeNodeEhrChainDatabase();
    }

}