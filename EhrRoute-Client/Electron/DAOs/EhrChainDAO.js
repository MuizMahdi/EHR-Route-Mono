var sqlite3 = require('sqlite3');

/*
*   EHRs chain DAO module -- 
*   Contains all low-level database writing and reading functions for EHRs
*/

var ehrDb = new sqlite3.Database('./node-records.chain');


module.exports = {

    initializeNodeEhrChainDatabase: function()
    {   
        console.log('[ Initializing EHRsChain DB ]');

        ehrDb.serialize(function() {
            // Create ehrs table if not exists
            ehrDb.run("CREATE TABLE IF NOT EXISTS node_ehr_chain (address TEXT, public_key TEXT, private_key TEXT)");
        });

        ehrDb.close();
    }

}

