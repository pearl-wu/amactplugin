
@objc(amactplugin) class amactplugin : CDVPlugin {
	//version openapp openweb openwebshow openwebclose getcookie setcookie clearcookie encode
	
    //start
    func version(_ command: CDVInvokedUrlCommand) {
        print("version")
        let msg = command.arguments[0] as? String ?? ""
        var data = ""
        //print("method call OK!")
        let infoDic = Bundle.main.infoDictionary
        if(msg == "version"){         // 取App的版本號
            let appVersion = infoDic?["CFBundleShortVersionString"]
            data = appVersion as! String
            // print(appVersion as Any)
            // 取App的build版本
            //let appBuildVersion = infoDic?["CFBundleVersion"]
            //print(appBuildVersion as Any)
        }else if(msg == "APPname"){   // 取App的名稱
            let appName = infoDic?["CFBundleDisplayName"]
            data = appName as! String
            //print(appName as Any)
        }
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,messageAs: data)
        self.commandDelegate!.send(pluginResult,callbackId: command.callbackId)
    }
	
    func getcookie(_ command: CDVInvokedUrlCommand) {
        print("getcookie")
       // self.cmd = command
       // self.msgUnReadInitDelaySend()
    }
	
    func setcookie(_ command: CDVInvokedUrlCommand) {
        print("setcookie")
       // self.cmd = command
       // self.msgUnReadInitDelaySend()
    }
	
    func clearcookie(_ command: CDVInvokedUrlCommand) {
        print("clearcookie")
       // self.cmd = command
       // self.msgUnReadInitDelaySend()
    }
	
    func encode(_ command: CDVInvokedUrlCommand) {
        print("encode")
       // self.cmd = command
       // self.msgUnReadInitDelaySend()
    }

}
