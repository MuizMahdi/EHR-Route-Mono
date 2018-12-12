const { app, BrowserWindow } = require("electron");
const {ipcMain} = require('electron');
const path = require("path");
const url = require("url");
let win;


ipcMain.on('testMessage', (event, arg) => {
   console.log("Message received on main with argument: " + arg);
   event.sender.send('testMessage2', 'sending back to renderer');
});

// Create window on electron initialization
app.on("ready", createWindow)


// Quit when all windows are closed
app.on("window-all-closed", function () {

   if (process.platform !== 'darwin') {
      app.quit()
   }

})


app.on("activate", function() {
   
   if (win === null) {
      createWindow()
   }

})


function createWindow()
{
   win = new BrowserWindow({
      width: 800,
      height: 600,
      icon: "https://image.flaticon.com/icons/svg/149/149054.svg" 
   });


   //win.loadURL(`file://${__dirname}/dist/EhrRoute-Client/index.html`);

   win.loadURL(
      url.format({
         pathname: path.join(__dirname, 'dist/index.html'),
         protocol: 'file:',
         slashes: true
      })
   )


   // Chrome DevTools.
   win.webContents.openDevTools()


   initializeDatabase();


   // On window closing set win to null
   win.on("closed", function() {
      win = null
   })
}


function initializeDatabase()
{
   var sqlite3 = require('sqlite3');
   var db = new sqlite3.Database('./database.sqlite3');

   //var db = new sqlite3.Database(app.getPath('userData')); // Stores database in application's data directory, commented for dev.
  
   db.serialize(function() {

      initAddress(db);

   });

   // Get USER ROLE on login
   // IF user role is [Admin] then create node_ehr_chain table if not exists

   db.close();
}


function initAddress(db)
{
   // Create address table if not exists all users with no matter what role must have it.
   db.run("CREATE TABLE IF NOT EXISTS address (address TEXT, public_key TEXT, private_key TEXT)");
}
