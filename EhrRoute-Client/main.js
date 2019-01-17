'use strict';

const { app, BrowserWindow } = require("electron");
const { ipcMain } = require('electron');
// require('electron-reload')(__dirname);

const path = require("path");
const url = require("url");


let win; // Window object


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


function createWindow()
{
   // Window properties
   win = new BrowserWindow({
      width: 800,
      height: 600,
      icon: "https://image.flaticon.com/icons/svg/149/149054.svg" 
   });


   // For development only, get dynamic version from localhost:4200.
   require('electron-reload')(__dirname, {
      electron: require(`${__dirname}/node_modules/electron`)
   });
   
   win.loadURL('http://localhost:4200');

/*
   // load the index.html from the dist folder of Angular
   win.loadURL(
      url.format({
         pathname: path.join(__dirname, 'dist/index.html'),
         protocol: 'file:',
         slashes: true
      })
   );
*/

   // Enable and open Chrome DevTools
   win.webContents.openDevTools();


   // On window closing set win to null
   win.on("closed", function() {
      win = null
   });
}
