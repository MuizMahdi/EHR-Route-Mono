const keysDbDAO = require('../DAOs/AddressKeysDAO');

module.exports = {

    initializeAddressKeys: function() {
        keysDbDAO.initializeAddressKeysDatabase();
    }

}