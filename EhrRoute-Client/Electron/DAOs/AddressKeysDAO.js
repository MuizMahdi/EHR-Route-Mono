var sqlite3 = require('sqlite3');

/*
*   Keys DAO module --
*   Contains all low-level database writing and reading functions for user public and private keys
*/

var keyDb = new sqlite3.Database('./user-address.keys');


module.exports = {

    initializeAddressKeysDatabase: function() 
    {
        console.log('[ Initializing AddressKeys DB ]');

        keyDb.serialize(function() {
            // Create address table if not exists
            keyDb.run("CREATE TABLE IF NOT EXISTS address (address TEXT, public_key TEXT, private_key TEXT)");
        });

        keyDb.close();
    }

}