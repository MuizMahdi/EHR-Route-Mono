const { app, BrowserWindow } = require("electron");
const path = require("path");
const url = require("url");

let win;

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
   // win.webContents.openDevTools()

   // On window closing set win to null
   win.on("closed", function() {
      win = null
   })
}


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
