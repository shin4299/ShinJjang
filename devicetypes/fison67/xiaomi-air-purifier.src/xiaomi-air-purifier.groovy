/**
 *  Xiaomi Air Purifier (v.0.0.1)
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
	definition (name: "Xiaomi Air Purifier", namespace: "fison67", author: "fison67") {
        capability "Switch"						//"on", "off"
         
        attribute "switch", "string"
        attribute "temperature", "string"
        attribute "humidity", "string"
        attribute "pm25", "string"
        attribute "buzzer", "string"
        attribute "mode", "string"
        attribute "ledBrightness", "string"
        attribute "f1_hour_used", "string"
        attribute "filter1_life", "string"
        attribute "average_aqi", "string"
        
        
        attribute "lastCheckin", "Date"
         
        command "setSpeed"
        command "setStatus"
        command "refresh"
        
        command "on"
        command "off"
        
        command "setModeAuto"
        command "setModeSilent"
        command "setModeFavorite"
        command "setModeIdle"
        
        command "buzzerOn"
        command "buzzerOff"
        
        command "ledOn"
        command "ledOff"
        
        command "setBright"
        command "setBrightDim"
        command "setBrightOff"
	}


	simulator {
	}

	tiles {
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'\n${name}', action:"switch.off", icon:"http://blogfiles.naver.net/MjAxODAzMjdfNzQg/MDAxNTIyMTMyNzMxMjEy.i1IvtTLdQ-Y3yHOyI0cwM0QKo8SobVo5vo0-zu72ZZkg.m7o9vNcIoiQBozog9FUXnE3w9O8U0kHeNxDeuWOfaWIg.PNG.shin4299/MiAirPurifier2S_on_tile.png?type=w1", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "off", label:'\n${name}', action:"switch.on", icon:"http://blogfiles.naver.net/MjAxODAzMjdfMTk4/MDAxNTIyMTMyNzMxMjEz.BdXDvyyncHtsRwYxAHHWI4zCZaGxYkKAcCbrRYvRtEcg.HHz2i2rn7IdfCFJd-5heHMCllb0TJgXAq8dHtdM1beEg.PNG.shin4299/MiAirPurifier2S_off_tile.png?type=w1", backgroundColor:"#ffffff", nextState:"turningOn"
                
                attributeState "turningOn", label:'\n${name}', action:"switch.off", icon:"http://blogfiles.naver.net/MjAxODAzMjdfMTk4/MDAxNTIyMTMyNzMxMjEz.BdXDvyyncHtsRwYxAHHWI4zCZaGxYkKAcCbrRYvRtEcg.HHz2i2rn7IdfCFJd-5heHMCllb0TJgXAq8dHtdM1beEg.PNG.shin4299/MiAirPurifier2S_off_tile.png?type=w1", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "turningOff", label:'\n${name}', action:"switch.on", icon:"http://blogfiles.naver.net/MjAxODAzMjdfNzQg/MDAxNTIyMTMyNzMxMjEy.i1IvtTLdQ-Y3yHOyI0cwM0QKo8SobVo5vo0-zu72ZZkg.m7o9vNcIoiQBozog9FUXnE3w9O8U0kHeNxDeuWOfaWIg.PNG.shin4299/MiAirPurifier2S_on_tile.png?type=w1", backgroundColor:"#ffffff", nextState:"turningOn"
			}
            
            tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}',icon: "st.Health & Wellness.health9")
            }
		}
        standardTile("switch2", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "on", label:'ON', action:"switch.off", icon:"https://postfiles.pstatic.net/MjAxODAzMjdfMjcy/MDAxNTIyMTMxNzU3MDk0.N_tjWtJELqei9aUS5a7GDAbood-9HRsE7CEyvOGW9gwg.8Kx4Sq9TB0MdEGwkuT4Pp5R1y85lfhhGh1mSW6DB4E8g.PNG.shin4299/MiAirPurifier2S_on.png?type=w580", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'OFF', action:"switch.on", icon:"https://postfiles.pstatic.net/MjAxODAzMjdfMjI3/MDAxNTIyMTMxNzU3MDkz._Am5iYSb9WU7jNHnFF-gy0-KvnsJZaGvEotWYhS6MOcg.uHiBGv8YpSE4zglfog873hRTw-4e59SA21xbyauSx4Eg.PNG.shin4299/MiAirPurifier2S_off.png?type=w580", backgroundColor:"#ffffff", nextState:"turningOn"
            state "turningOff", label:'turningOff', action:"switch.on", icon:"https://postfiles.pstatic.net/MjAxODAzMjdfMjcy/MDAxNTIyMTMxNzU3MDk0.N_tjWtJELqei9aUS5a7GDAbood-9HRsE7CEyvOGW9gwg.8Kx4Sq9TB0MdEGwkuT4Pp5R1y85lfhhGh1mSW6DB4E8g.PNG.shin4299/MiAirPurifier2S_on.png?type=w580", backgroundColor:"#00a0dc", nextState:"turningOn"
            state "turningOn", label:'turningOn', action:"switch.off", icon:"https://postfiles.pstatic.net/MjAxODAzMjdfMjI3/MDAxNTIyMTMxNzU3MDkz._Am5iYSb9WU7jNHnFF-gy0-KvnsJZaGvEotWYhS6MOcg.uHiBGv8YpSE4zglfog873hRTw-4e59SA21xbyauSx4Eg.PNG.shin4299/MiAirPurifier2S_off.png?type=w580", backgroundColor:"#ffffff", nextState:"turningOff"
        }
        valueTile("temperature", "device.temperature", width: 2, height: 2, unit: "°C") {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        
        valueTile("humidity", "device.humidity", width: 2, height: 2, unit: "%") {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        
         valueTile("pm25", "device.pm25", width: 2, height: 2, unit: "") {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }
        
        standardTile("led", "device.led", width: 2, height: 2, canChangeIcon: true) {
            state "on", label:'Led', action:"ledOff", icon:"st.switches.light.on", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'Led', action:"ledOn", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
             
        	state "turningOn", label:'${name}', action:"ledOff", icon:"st.switches.light.on", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "turningOff", label:'${name}', action:"ledOn", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
        }
        
        standardTile("buzzer", "device.buzzer", inactiveLabel: false, width: 2, height: 2, canChangeIcon: true) {
            state "on", label:'Sound', action:"buzzerOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'Mute', action:"buzzerOn", backgroundColor:"#ffffff", nextState:"turningOn"
             
        	state "turningOn", label:'....', action:"buzzerOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "turningOff", label:'....', action:"buzzerOn", backgroundColor:"#ffffff", nextState:"turningOn"
        }
        
        standardTile("mode", "device.mode", width: 2, height: 2, canChangeIcon: true) {
            state "idle", label: 'Idle', action: "setModeAuto", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"auto"
            state "auto", label: 'Auto', action: "setModeSilent", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"silent"
            state "silent", label: 'Silent', action: "setModeFavorite", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"favorite"
            state "favorite", label: 'Manual', action: "setModeAuto", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"auto"
        }
        
        standardTile("ledBrightness", "device.ledBrightness", width: 2, height: 2, canChangeIcon: true) {
            state "bright", label: 'Bright', action: "setBrightDim", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"dim"
            state "dim", label: 'Dim', action: "setBrightOff", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"off"
            state "off", label: 'Off', action: "setBright", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"bright"
        } 
                
        controlTile("speed", "device.speed", "slider",  height: 2, width: 2, inactiveLabel: false, range:"(0..16)") {
            state "val", action:"setSpeed", label:"${currentValue}"
        }
        
        standardTile("temp1", "device.ledBrightness", width: 2, height: 2) {
        } 
        
        standardTile("f1_hour_used_name", "device.f1_hour_used_name", inactiveLabel: false, width: 2, height: 1) {
            state "temp", label:'Filter Hour Used',  backgroundColor:"#ffffff"
        }
        
        standardTile("filter1_life_name", "device.filter1_life_name", inactiveLabel: false, width: 2, height: 1) {
            state "temp", label:'Filter Life Remain',  backgroundColor:"#ffffff"
        }
        
        standardTile("average_aqi_name", "device.average_aqi_name", inactiveLabel: false, width: 2, height: 1) {
            state "temp", label:'Average Aqi',  backgroundColor:"#ffffff"
        }
        
        valueTile("f1_hour_used", "device.f1_hour_used", width: 2, height: 2) {
            state("val", label:'${currentValue}', defaultState: true, backgroundColor:"#00a0dc")
        }
        
        valueTile("filter1_life", "device.filter1_life", width: 2, height: 2) {
            state("val", label:'${currentValue}', defaultState: true, backgroundColor:"#00a0dc")
        }
        
        valueTile("average_aqi", "device.average_aqi", width: 2, height: 2) {
            state("val", label:'${currentValue}', defaultState: true, backgroundColor:"#00a0dc")
        }
   	main (["switch2"])
	details(["switch", "temperature", "humidity", "pm25", "led", "buzzer", "mode", "ledBrightness", "speed", 
    		 "temp1", "f1_hour_used_name", "filter1_life_name", "average_aqi_name", 
             "f1_hour_used", "filter1_life", "average_aqi"])
        
	}
}

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
    log.debug "${params.key} : ${params.data}"
 
 	switch(params.key){
    case "mode":
    	sendEvent(name:"mode", value: params.data )
    	break;
    case "pm2.5":
    	sendEvent(name:"pm25", value: params.data + "㎍/㎥")
    	break;
    case "aqi":
    	sendEvent(name:"pm25", value: params.data + "㎍/㎥")
    	break;
    case "relativeHumidity":
    	sendEvent(name:"humidity", value: params.data + "%")
    	break;
    case "power":
    	sendEvent(name:"switch", value: (params.data == "true" ? "on" : "off"))
    	break;
    case "temperature":
        sendEvent(name:"temperature", value: params.data)
    	break;
    case "buzzer":
        sendEvent(name:"buzzer", value: (params.data == "true" ? "on" : "off"))
    	break;
    case "ledBrightness":
        sendEvent(name:"ledBrightness", value: params.data)
    	break;
    case "speed":
        sendEvent(name:"speed", value: params.data)
    	break;
    case "led":
        sendEvent(name:"led", value: (params.data == "true" ? "on" : "off"))
    	break;
    case "f1_hour_used":
    	sendEvent(name:"f1_hour_used", value: params.data)
        break;
    case "filter1_life":
    	sendEvent(name:"filter1_life", value: params.data)
    	break;
    case "average_aqi":
    	sendEvent(name:"average_aqi", value: params.data + "㎍/㎥")
    	break;
    }
    
    def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now)
}

def refresh(){
	log.debug "Refresh"
    def options = [
     	"method": "GET",
        "path": "/get?id=${state.id}",
        "headers": [
        	"HOST": state.app_url,
            "Content-Type": "application/json"
        ]
    ]
    sendCommand(options, callback)
}

def setSpeed(speed){
	log.debug "setSpeed >> ${state.id}, speed=" + speed
    def body = [
        "id": state.id,
        "cmd": "speed",
        "data": speed
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
	sendEvent(name: "level", value: speed)
}

def setModeSilent(){
    log.debug "setModeSilent >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "mode",
        "data": "silent"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setModeFavorite(){
	log.debug "setModeFavorite >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "mode",
        "data": "favorite"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setModeIdle(){
	log.debug "setModeIdle >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "mode",
        "data": "idle"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setModeAuto(){
	log.debug "setModeAuto >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "mode",
        "data": "auto"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def buzzerOn(){
	log.debug "buzzerOn >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "buzzer",
        "data": "on"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def buzzerOff(){
	log.debug "buzzerOff >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "buzzer",
        "data": "off"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def ledOn(){
	log.debug "ledOn >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "led",
        "data": "on"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def ledOff(){
	log.debug "ledOff >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "led",
        "data": "off"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setBright(){
	log.debug "setBright >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "ledBrightness",
        "data": "bright"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setBrightDim(){
	log.debug "setDim >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "ledBrightness",
        "data": "brightDim"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setBrightOff(){
	log.debug "setDim >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "ledBrightness",
        "data": "off"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def on(){
	log.debug "Off >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "power",
        "data": "on"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def off(){
	log.debug "Off >> ${state.id}"
	def body = [
        "id": state.id,
        "cmd": "power",
        "data": "off"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg
    try {
        msg = parseLanMessage(hubResponse.description)
		def jsonObj = new JsonSlurper().parseText(msg.body)
        log.debug jsonObj
//        setStatus(jsonObj.state)
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

def makeCommand(body){
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