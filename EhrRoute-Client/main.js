'use strict';

const { app, BrowserWindow, ipcMain } = require("electron");
const { download } = require("electron-dl");
// require('electron-reload')(__dirname);

const path = require("path");
const url = require("url");
const fs = require("fs");


// Window object
let appWindow; 


// Create window on electron initialization
app.on("ready", createWindow);


// Initialize window and its properties on startup
app.on("activate", function() {
   
   if (appWindow === null) {
      createWindow();
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
   // Application window properties
   appWindow = new BrowserWindow({
      width: 800,
      height: 600,
      icon: "https://image.flaticon.com/icons/svg/149/149054.svg" 
   });


   // [Dev only] get dynamic version from localhost:4200.
   require('electron-reload')(__dirname, {
      electron: require(`${__dirname}/node_modules/electron`)
   });
   
   appWindow.loadURL('http://localhost:4200');


/*
   // load the index.html from the dist folder of Angular
   appWindow.loadURL(
      url.format({
         pathname: path.join(__dirname, '/dist/index.html'),
         protocol: 'file:',
         slashes: true
      })
   );
*/

   // Enable and open Chrome DevTools
   appWindow.webContents.openDevTools();


   // [Dev only] Disable menu 
   appWindow.setMenu(null);


   // On window closing set appWindow to null
   appWindow.on("closed", function() {
      appWindow = null
   });


   // On file download requests
   ipcMain.on("download", (event, info) => {

      const appWindow = BrowserWindow.getFocusedWindow();

      download(appWindow, info.url, info.properties).then(dl => {
         event.sender.send('DownloadComplete', dl.getSavePath());
      });
      
   });
}
