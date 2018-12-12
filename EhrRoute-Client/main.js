const { app, BrowserWindow } = require("electron");
const { ipcMain } = require('electron');

const path = require("path");
const url = require("url");

const keyService = require('./Electron/Services/AddressKeyService');
const ehrService = require('./Electron/Services/EhrChainService');

let win;


// Create window on electron initialization
app.on("ready", createWindow);


// Quit when all windows are closed
app.on("window-all-closed", function () {

   if (process.platform !== 'darwin') {
      app.quit()
   }

});


app.on("activate", function() {
   
   if (win === null) {
      createWindow()
   }

});

function createWindow()
{
   win = new BrowserWindow({
      width: 800,
      height: 600,
      icon: "https://image.flaticon.com/icons/svg/149/149054.svg" 
   });


   win.loadURL(
      url.format({
         pathname: path.join(__dirname, 'dist/index.html'),
         protocol: 'file:',
         slashes: true
      })
   );


   // Open Chrome DevTools.
   win.webContents.openDevTools();


   // Create address table. All users with all roles must have it
   keyService.initializeAddressKeys();


   // On window closing set win to null
   win.on("closed", function() {
      win = null
   });
}
 


ipcMain.on('Create_Node_EHR_Chain_DB', (event, arg) => {

   ehrService.initializeNodeEhrChain();
   
});
