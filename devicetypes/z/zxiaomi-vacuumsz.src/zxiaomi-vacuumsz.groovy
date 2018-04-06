/**
*  Xiaomi Vacuums (v.0.0.1)
*
* MIT License
*
* Copyright (c) 2018 fison67@nate.com
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
* 
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*/

import groovy.json.JsonSlurper

metadata {
    definition (name: "zzXiaomi Vacuumszz", namespace: "z", author: "z") {
       capability "Switch"    
        
        capability "Filter Status"          // onaldo
        capability "Fan Speed"              // onaldo
        
       attribute "switch", "string"
       
       attribute "status", "string"
       attribute "battery", "number"
       attribute "clean_time", "string"
       attribute "clean_area", "string"
       attribute "in_cleaning", "string"
       attribute "main_brush_work_time", "string"
       attribute "side_brush_work_time", "string"
       attribute "filterWorkTime", "string"
       attribute "sensorDirtyTime", "string"
       
       attribute "lastCheckin", "Date"
        
       command "on"
       command "off"
       
       command "find"
       command "clean"
       command "charge"
       command "paused"
       command "fanSpeed"
       command "spotClean"
       
       command "quiet"
       command "balanced"
       command "turbo"
       command "fullSpeed"
       command "setVolume"
       command "setVolumeWithTest"
    }

    simulator {
       
   }
   
    preferences {
        input name:"model", type:"enum", title:"Select Model", options:["Vacuum Cleaner", "Vacuum Cleaner 2"], description:"Select Your Vacuum Cleaner Model"
    }

    tiles {
        multiAttributeTile(name:"mode", type: "generic", width: 6, height: 4, canChangeIcon: true){
            tileAttribute ("device.mode", key: "PRIMARY_CONTROL") {
               attributeState "initiating", label:'${name}', backgroundColor:"#00a0dc", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"off"
               attributeState "charger-offline", label:'${name}', backgroundColor:"#000000", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"on"
               attributeState "waiting", label:'${name}',  backgroundColor:"#00a0dc", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"charge"
               attributeState "cleaning", label:'${name}', backgroundColor:"#4286f4", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_on.png?raw=true", action:"off"
               attributeState "returning", label:'${name}', backgroundColor:"#4e25a8", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_turning_off.png?raw=true", action:"on"
               attributeState "charging", label:'${name}',   backgroundColor:"#25a896", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"on"
               
               attributeState "charging-error", label:'${name}',  backgroundColor:"#ff2100", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"on"
               attributeState "paused", label:'${name}',  backgroundColor:"#09540d", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"on"
               
               attributeState "spot-cleaning", label:'${name}', backgroundColor:"#a0e812", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_on.png?raw=true", action:"off"
               attributeState "error", label:'${name}',   backgroundColor:"#ff2100", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_off.png?raw=true", action:"on"
               
               attributeState "shutting-down", label:'${name}',  backgroundColor:"#00a0dc", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_turning_off.png?raw=true", action:"on"
               attributeState "updating", label:'${name}',  backgroundColor:"#ffa0ea", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_turning_off.png?raw=true", action:"on"
               
               attributeState "docking", label:'${name}', backgroundColor:"#9049bc", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_turning_off.png?raw=true", action:"off"
               attributeState "zone-cleaning", label:'${name}',  backgroundColor:"#91f268", icon:"https://github.com/fison67/mi_connector/blob/master/icons/vacuum_on.png?raw=true", action:"off"
               
               attributeState "full", label:'${name}', backgroundColor:"#ffffff", icon:"st.Electronics.electronics1", action:"on"
               
            }
           
           tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
               attributeState("default", label:'Updated: ${currentValue}',icon: "st.Health & Wellness.health9")
           }
           
        }
       
// *****************************************************************
        
       standardTile("switch", "device.switch", inactiveLabel: false, width: 2, height: 2, canChangeIcon: true) {
           state "on", label:'${name}', action:"off", backgroundColor:"#00a0dc", nextState:"turningOff"
           state "off", label:'${name}', action:"on", backgroundColor:"#ffffff", nextState:"turningOn"
            
           state "turningOn", label:'....', action:"off", backgroundColor:"#00a0dc", nextState:"turningOff"
           state "turningOff", label:'....', action:"on", backgroundColor:"#ffffff", nextState:"turningOn"
       }
       
       standardTile("fanSpeed", "device.fanSpeed", inactiveLabel: false, width: 1, height: 1) {
           state "quiet", label:'Quiet', action:"balanced", backgroundColor:"#00a0dc", nextState:"balanced"
           state "balanced", label:'Balanced', action:"turbo", backgroundColor:"#1000ff", nextState:"turbo"
           state "turbo", label:'Turbo', action:"fullSpeed", backgroundColor:"#9a71f2", nextState:"fullSpeed"
           state "fullSpeed", label:'Max', action:"quiet", backgroundColor:"#aa00ff", nextState:"quiet"
       }    

//*********************onaldo******************************
        
       valueTile("quiet_label", "", decoration: "flat") {
           state "default", label:'Quiet'
       }
        
       valueTile("balanced_label", "", decoration: "flat") {
           state "default", label:'Balanced'
       }
        
       valueTile("turbo_label", "", decoration: "flat") {
           state "default", label:'Turbo'
       }
        
        valueTile("fullSpeed_label", "", decoration: "flat") {
           state "default", label:'FullSpeed'
       }
 
    
    // 여기부터
    
      valueTile("spot_label", "", decoration: "flat") {
           state "default", label:'Spot'
       }
      
      valueTile("paused_label", "", decoration: "flat") {
           state "default", label:'Paused/\nResume'
       }
      
      valueTile("charge_label", "", decoration: "flat") {
           state "default", label:'Carge'
       }
      
      valueTile("findme_label", "", decoration: "flat") {
           state "default", label:'Find me'
       }
      
      valueTile("battery_label", "", decoration: "flat") {
           state "default", label:'Battery'
       }
      
      valueTile("speed_label", "", decoration: "flat") {
           state "default", label:'Speed'
       } 
      
      // 여기까지
        
       standardTile("quiet_mode", "device.fanSpeed", decoration: "flat") {
         state "off", label: "off", action: "quiet", icon:"st.unknown.zwave.static-controller", backgroundColor:"#bcbcbc", nextState:"quiet"
            state "quiet", label: "on", action: "off", icon:"st.unknown.zwave.static-controller", backgroundColor:"#73C1EC", nextState:"off"
        }

       standardTile("balanced_mode", "device.fanSpeed", decoration: "flat") {
         state "off", label: "off", action: "balanced", icon:"st.unknown.zwave.static-controller", backgroundColor:"#bcbcbc", nextState:"balanced"
            state "balanced", label: "on", action: "off", icon:"st.quirky.spotter.quirky-spotter-sound-off", backgroundColor:"#6eca8f", nextState:"off"
       }

       standardTile("turbo_mode", "device.fanSpeed", decoration: "flat") {
         state "off", label: "off", action: "turbo", icon:"st.unknown.zwave.static-controller", backgroundColor:"#bcbcbc", nextState:"turbo"
            state "turbo", label: "on", action: "off", icon:"st.quirky.spotter.quirky-spotter-luminance-light", backgroundColor:"#f9b959", nextState:"off"
       }
       
       standardTile("fullspeed_mode", "device.fanSpeed", decoration: "flat") {
            state "off", label: "off", action: "fullSpeed", icon:"st.unknown.zwave.static-controller", backgroundColor:"#bcbcbc", nextState:"fullSpeed"
            state "fullSpeed", label: "on", action: "off", icon:"st.Weather.weather1", backgroundColor:"#db5764", nextState:"off"  
       }

        standardTile("charge", "device.mode", decoration: "flat", width: 1, height: 1 ) {
           state "charge", label:'Charge', action:"charge",  backgroundColor:"#25a896"//, nextState:"returning"
//           state "returning", label:'returning',  backgroundColor:"#25a896"
//           state "charging", label:'charging',  backgroundColor:"#25a896"
       }

        standardTile("spot", "device.spot", decoration: "flat", width: 1, height: 1 ) {
           state "spot", label:'Spot', action:"spotClean",  backgroundColor:"#2ca6e8"
       }    
            
        standardTile("paused", "device.paused", decoration: "flat", width: 1, height: 1) {
           state "paused", label:'paused', action:"paused", backgroundColor:"#00a0dc", nextState:"restart"
           state "restart", label:'${name}', action:"on", backgroundColor:"#09540d"
       }    
            
        standardTile("find", "device.find", decoration: "flat", width: 1, height: 1 ) {
           state "find", label:'Find Me', action:"find",  backgroundColor:"#1cffe8"
       }    
        
       valueTile("battery", "device.battery",  height: 1, width: 1) {
           state "val", label:'${currentValue}', defaultState: true,
               backgroundColors:[
                   [value: 10, color: "#ff002a"],
                   [value: 20, color: "#f4425f"],
                   [value: 30, color: "#ef7085"],
                   [value: 40, color: "#ea8f9e"],
                   [value: 50, color: "#edadb7"],
                   [value: 60, color: "#a9aee8"],
                   [value: 70, color: "#7f87e0"],
                   [value: 80, color: "#505bd3"],
                   [value: 90, color: "#2131e0"]
               ]
        }

       controlTile("volume", "device.volume", "slider", height: 1, width: 1, inactiveLabel: false, range:"(0..100)") {
            state ("volume", label:'${currentValue}', action:"setVolumeWithTest")
        }

        valueTile("Filter","",decoration : "flat", width: 2, height: 2) {
          state "default", label: 'Filter'
       }
                          
        valueTile("filter_used", "device.filter_used", width: 2, height: 1) {
          state("val", label:'${currentValue} hours', defaultState: true, backgroundColor:"#bcbcbc")
       }
        
        valueTile("f_usage_label", "", decoration: "flat", width: 2, height:1) {
           state "default", label:'Usage \nTime'
        }
        
        valueTile("filter_life", "device.filter_life", width: 2, height: 1) {
          state("val", label:'${currentValue} hours', defaultState: true, backgroundColor:"#bcbcbc")
       }
        
        valueTile("f_remain_label", "", decoration: "flat", width:2, height:1) {
           state "default", label:'Remaining \nTime'
        }        
        
        valueTile("Side brush","",decoration : "flat", width: 2, height: 2) {
          state "default", label: 'Side brush'
       }
        
        valueTile("s_brush_used", "device.S_brush_used", width: 3, height: 1) {
          state("val", label:'${currentValue} hours', defaultState: true, backgroundColor:"#bcbcbc")
       }
        
        valueTile("s_usage_label", "", decoration: "flat", width: 3, height:1) {
           state "default", label:'Side Brush\nUsage Time'
        }
        
        valueTile("s_brush_life", "device.S_brush_life", width: 3, height: 1) {
          state("val", label:'${currentValue} hours', defaultState: true, backgroundColor:"#bcbcbc")
       }
        
        valueTile("s_remain_label", "", decoration: "flat", width:2, height:1) {
           state "default", label:'Remaining \nTime'
        }    
            

        valueTile("Main brush","",decoration : "flat", width: 2, height: 2) {
          state "default", label: 'Main brush'
       }
        
        valueTile("m_brush_used", "device.M_brush_used", width: 3, height: 1) {
          state("val", label:'${currentValue} hours', defaultState: true, backgroundColor:"#bcbcbc")
       }
        
        valueTile("m_usage_label", "", decoration: "flat", width: 3, height:1) {
           state "default", label:'Main Brush\nUsage Time'
        }
        
        valueTile("m_brush_life", "device.M_brush_life", width: 3, height: 1) {
          state("val", label:'${currentValue} hours', defaultState: true, backgroundColor:"#bcbcbc")
       }
        
        valueTile("m_remain_label", "", decoration: "flat", width:2, height:1) {
           state "default", label:'Remaining \nTime'
        }
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label:"", action:"refresh", icon:"st.secondary.refresh"
        }
        

        
       main (["switch"])
         details(["mode", "switch", "quiet_label", "balanced_label", "turbo_label", "fullSpeed_label", 
                  "quiet_mode", "balanced_mode", "turbo_mode", "fullspeed_mode", 
                  "spot_label", "paused_label", "charge_label", "findme_label", "battery_label", "speed_label", 
                  "spot", "paused", "charge", "find", "battery", "volume", 
                //  "Filter", "f_usage_label", "filter_used", 
                  //          "f_remain_label", "filter_life",
                  "m_usage_label", "m_brush_used", 
                    //  "Main brush",           "m_remain_label", "m_brush_life", 
                   "s_usage_label", "s_brush_used", 
                      //  "Side brush",        "s_remain_label","s_brush_life", "refresh"])
    }
}

//****************************여기가어렵군**************************************************

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

def setInfo(String app_url, String id) {
    log.debug "${app_url}, ${id}"
    state.app_url = app_url
   state.id = id
}

def setStatus(params){
    log.debug "${params.key} >> ${params.data}"
   
    switch(params.key){
   case "mode":
       sendEvent(name:"mode", value: params.data )
       if(params.data == "paused"){
           sendEvent(name:"switch", value: "paused" )
       }
       break;
   case "battery":
       sendEvent(name:"battery", value: params.data + "%" )
       break;
       
   case "fanSpeed":
    	def val = params.data.toInteger()
        def _value
        switch(val){
        case 38:
        	_value = "quiet"
        	break;
        case 60:
        	_value = "balanced"
        	break;
        case 77:
        	_value = "turbo"
        	break;
        case 90:
        	_value = "fullSpeed"
        	break;
        }
    	sendEvent(name:"fanSpeed", value: _value )
    	break;
   case "cleaning":
       sendEvent(name:"switch", value: (params.data == "true" ? "on" : "off") )
          sendEvent(name:"paused", value: params.data == "true" ? "restart" : "paused" )     
       break;
   case "volume":
       sendEvent(name:"volume", value: params.data )
       break;
    case "f1_hour_used":
        def para = "${params.data}"
        String data = para
        def stf = Float.parseFloat(data)
        def use = Math.round(stf/24)    
       sendEvent(name:"f1_hour_used", value: use)
       break;
    case "filter1_life":
        def para = "${params.data}"
        String data = para
        def stf = Float.parseFloat(data)
        def life = Math.round(stf*1.435)    
       sendEvent(name:"filter1_life", value: life)
       break;
   }
   
   def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
   sendEvent(name: "lastCheckin", value: now)
}

def refresh(){
	log.debug "Refresh"
    def options = [
     	"method": "GET",
        "path": "/devices/get/${state.id}",
        "headers": [
        	"HOST": state.app_url,
            "Content-Type": "application/json"
        ]
    ]
    sendCommand(options, callback)
}

def setVolume(volume){
    log.debug "setVolume >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "volume",
       "data": volume
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def setVolumeWithTest(volume){
    log.debug "setVolume >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "volumeWithTest",
       "data": volume
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def quiet(){
   log.debug "quiet >> ${state.id}"
   sendEvent(name:"fanSpeed", value: off )
   sendEvent(name:"fanSpeed", value: quiet )    
   def body = [
       "id": state.id,
       "cmd": "quiet"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def balanced(){
   log.debug "balanced >> ${state.id}"
   sendEvent(name:"fanSpeed", value: off )
   sendEvent(name:"fanSpeed", value: balanced )
   def body = [
       "id": state.id,
       "cmd": "balanced"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def turbo(){
   log.debug "turbo >> ${state.id}"
   sendEvent(name:"fanSpeed", value: off )
   sendEvent(name:"fanSpeed", value: turbo )     
   def body = [
       "id": state.id,
       "cmd": "turbo"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def fullSpeed(){
   log.debug "fullSpeed >> ${state.id}"
   sendEvent(name:"fanSpeed", value: off )
   sendEvent(name:"fanSpeed", value: fullSpeed )
   def body = [
       "id": state.id,
       "cmd": "fullSpeed"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def spotClean(){
    log.debug "spotClean >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "spotClean"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
   
   sendEvent(name:"spot", value: "on" )
}

def charge(){
    log.debug "charge >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "charge"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def paused(){
    log.debug "paused >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "pause"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def start(){
   log.debug "start >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "start"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def find(){
   log.debug "find >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "find"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def on(){
        log.debug "On >> ${state.id}"
   def body = [
       "id": state.id,
       "cmd": "clean"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

def off(){
    log.debug "Off >> ${state.id}"
    def body = [
       "id": state.id,
       "cmd": "stop"
   ]
   def options = makeCommand(body)
   sendCommand(options, null)
}

/*
def timer(mSecond, function){
    def now = new Date()
    def runTime = new Date(now.getTime() + mSecond)
    runOnce(runTime, function);
}
{"properties":{"batteryLevel":100,"charging":false,"cleaning":true,"error":{"id":3,"description":"Unknown error 3"},"fanSpeed":38},"state":{"error":null,"state":"cleaning","batteryLevel":100,"cleanTime":0,"cleanArea":0,"fanSpeed":38,"in_cleaning":0,"mainBrushWorkTime":308198,"sideBrushWorkTime":308198}}
*/

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg
    try {
        msg = parseLanMessage(hubResponse.description)
		def jsonObj = new JsonSlurper().parseText(msg.body)
                 
        sendEvent(name:"battery", value: jsonObj.properties.batteryLevel)
        sendEvent(name:"m_brush_used", value: Math.round(jsonObj.state.mainBrushWorkTime/3600))
        sendEvent(name:"s_brush_used", value: Math.round(jsonObj.state.sideBrushWorkTime/3600))        
        sendEvent(name:"mode", value: jsonObj.state.state)
        log.debug (jsonObj.properties.cleaning ? "on" : "off")
        sendEvent(name:"switch", value: (jsonObj.properties.cleaning ? "on" : "off") )
       	sendEvent(name:"paused", value: jsonObj.properties.cleaning ? "paused" : "restart" )  
        
        def fanSpeed;
        switch(jsonObj.state.fanSpeed){
        case 38:
        	fanSpeed = "quiet"
        	break;
        case 60:
        	fanSpeed = "balanced"
        	break;
        case 77:
        	fanSpeed = "turbo"
        	break;
        case 90:
        	fanSpeed = "fullSpeed"
        	break;
        }
    	sendEvent(name:"fanSpeed", value: fanSpeed )
        
        updateLastTime()
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}


def updated() {
               
           }

def sendCommand(options, _callback){
    def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
   sendHubCommand(myhubAction)
}

def makeCommand(body) {
    def options = [
        "method": "POST",
       "path": "/control",
       "headers": [
           "HOST": state.app_url,
           "Content-Type": "application/json"
       ],
       "body":body
   ]
   return options
}
