'use strict';

const { app, BrowserWindow, ipcMain } = require("electron");
const { download } = require("electron-dl");
// require('electron-reload')(__dirname);

const path = require("path");
const url = require("url");


// Window object
let win; 


// Create window on electron initialization
app.on("ready", createWindow);


// Initialize window and its properties on startup
app.on("activate", function() {
   
   if (win === null) {
      createWindow()
   }

});


// Quit when all windows are closed
app.on("window-all-closed", function () {

   if (process.platform !== 'darwin') {
      app.quit()
   }

});


async function createWindow()
{
   // Window properties
   win = new BrowserWindow({
      width: 800,
      height: 600,
      icon: "https://image.flaticon.com/icons/svg/149/149054.svg" 
   });


   // [Dev only] get dynamic version from localhost:4200.
   require('electron-reload')(__dirname, {
      electron: require(`${__dirname}/node_modules/electron`)
   });
   
   win.loadURL('http://localhost:4200');


/*
   // load the index.html from the dist folder of Angular
   win.loadURL(
      url.format({
         pathname: path.join(__dirname, '/dist/index.html'),
         protocol: 'file:',
         slashes: true
      })
   );
*/

   // Enable and open Chrome DevTools
   win.webContents.openDevTools();


   // [Dev only] Disable menu 
   win.setMenu(null);


   // On window closing set win to null
   win.on("closed", function() {
      win = null
   });


   // On file download requests
   ipcMain.on("download", (event, info) => {

      const win = BrowserWindow.getFocusedWindow();

      download(win, info.url, info.properties).then(dl => {
         event.sender.send('DownloadComplete', dl.getSavePath());
      });
      
   });
}
