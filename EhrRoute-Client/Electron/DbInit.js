var sqlite3 = require('sqlite3').verbose();

// Initialize a new database
var db = new sqlite3.Database('./database.sqlite3');

db.serialize(function() {

   db.run("CREATE TABLE test (text TEXT)");

   var stmt = db.prepare("INSERT INTO test VALUES (?)");

   for (var i = 0; i < 10; i++) 
   {
      stmt.run("Ipsum " + i);
   }

   stmt.finalize();

   db.each("SELECT rowid AS id, text FROM test", function(err, row) {
      console.log(row.id + ": " + row.text);
   });
});x

db.close();