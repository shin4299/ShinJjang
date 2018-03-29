/**
 *  Xiaomi Fan(v.0.0.1)
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
	definition (name: "Xiaomi Fan", namespace: "fison67", author: "fison67") {
        capability "Switch"						//"on", "off"
        capability "Switch Level"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
		capability "Fan Speed"
		capability "Refresh"
		capability "Sensor"
         
        attribute "buzzer", "string"
        attribute "ledBrightness", "string"
        attribute "speedLevel", "string"
        attribute "naturalLevel", "string"
        attribute "speed", "string"
        attribute "powerOffTime", "string"
        attribute "acPower", "string"
        attribute "batteryLevel", "string"
        attribute "childLock", "string"
        
        attribute "lastCheckin", "Date"
         
        command "on"
        command "off"
        
        command "buzzerOn"
        command "buzzerOff"
        
        command "setBright"
        command "setBrightDim"
        command "setBrightOff"
        
        command "setFanSpeed"
        command "setFanNatural"
        command "setAngleLevel"
        command "setAngleOn"
        command "setAngleOff"
        command "setMoveLeft"
        command "setMoveRight"
	}


	simulator {
	}

	tiles {
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"off", icon:"st.switches.light.on", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
                
                attributeState "turningOn", label:'${name}', action:"off", icon:"st.switches.light.on", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
			}
            
            tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}',icon: "st.Health & Wellness.health9")
            }
            tileAttribute ("device.fanSpeed", key: "SLIDER_CONTROL") {
                attributeState "level", action:"FanSpeed.setFanSpeed"
            }            
		}
        standardTile("switch2", "device.switch", inactiveLabel: false, width: 2, height: 2) {
            state "on", label:'ON', action:"switch.off", icon:"st.Appliances.appliances17", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'OFF', action:"switch.on", icon:"st.Appliances.appliances17", backgroundColor:"#ffffff", nextState:"turningOn"
             
        	state "turningOn", label:'turningOn', action:"switch.off", icon:"st.Appliances.appliances17", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "turningOff", label:'turningOff', action:"switch.on", icon:"st.Appliances.appliances17", backgroundColor:"#ffffff", nextState:"turningOn"
        }
        valueTile("temp_label", "", decoration: "flat") {
            state "default", label:'온도'
        }
        valueTile("humi_label", "", decoration: "flat") {
            state "default", label:'습도'
        }
        valueTile("low_label", "", decoration: "flat") {
            state "default", label:'Low \nMode'
        }
        valueTile("medium_label", "", decoration: "flat") {
            state "default", label:'Medium \nMode'
        }
        valueTile("high_label", "", decoration: "flat") {
            state "default", label:'High \nMode'
        }
        valueTile("strong_label", "", decoration: "flat") {
            state "default", label:'Strong \nMode'
        }
        valueTile("temperature", "device.temperature") {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 0, color: "#153591"],
                    [value: 5, color: "#153591"],
                    [value: 10, color: "#1e9cbb"],
                    [value: 20, color: "#90d2a7"],
                    [value: 30, color: "#44b621"],
                    [value: 40, color: "#f1d801"],
                    [value: 70, color: "#d04e00"],
                    [value: 90, color: "#bc2323"]
                ]
            )
        }
        valueTile("humidity", "device.humidity") {
            state("val", label:'${currentValue}', defaultState: true, 
            	backgroundColors:[
                    [value: 0, color: "#153591"],
                    [value: 20, color: "#1e9cbb"],
                    [value: 40, color: "#90d2a7"],
                    [value: 50, color: "#44b621"],
                    [value: 60, color: "#f1d801"],
                    [value: 80, color: "#d04e00"],
                    [value: 90, color: "#bc2323"]
                ]
            )
        }   
        standardTile("speed1", "device.fanSpeed") {
			state "default", label: "Low", action: "setFanSpeed(15)", icon:"st.quirky.spotter.quirky-spotter-luminance-dark", backgroundColor:"#FFDE61"
		}
        standardTile("speed2", "device.fanSpeed") {
			state "default", label: "Medium", action: "setFanSpeed.35", icon:"st.quirky.spotter.quirky-spotter-luminance-light", backgroundColor:"#f9b959"
		}
        standardTile("speed3", "device.fanSpeed") {
			state "default", label: "High", action: "setFanSpeed(70)", icon:"st.quirky.spotter.quirky-spotter-luminance-bright", backgroundColor:"#ff9eb2"
		}
        standardTile("speed4", "device.fanSpeed") {
			state "default", label: "Strong", action: "setFanSpeed(100)", icon:"st.Weather.weather1", backgroundColor:"#db5764"
		}
        valueTile("led_label", "", decoration: "flat") {
            state "default", label:'Brightness \nMode'
        }
        valueTile("buzzer_label", "", decoration: "flat") {
            state "default", label:'Buzzer \nMode'
        }        
        
        standardTile("buzzer", "device.buzzer", inactiveLabel: false, width: 2, height: 2, canChangeIcon: true) {
            state "on", label:'Sound', action:"buzzerOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "off", label:'Mute', action:"buzzerOn", backgroundColor:"#ffffff", nextState:"turningOn"
             
        	state "turningOn", label:'....', action:"buzzerOff", backgroundColor:"#00a0dc", nextState:"turningOff"
            state "turningOff", label:'....', action:"buzzerOn", backgroundColor:"#ffffff", nextState:"turningOn"
        }
        
        standardTile("ledBrightness", "device.ledBrightness", width: 2, height: 2, canChangeIcon: true) {
            state "bright", label: 'Bright', action: "setBrightDim", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"dim"
            state "dim", label: 'Dim', action: "setBrightOff", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"off"
            state "off", label: 'Off', action: "setBright", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"bright"
        } 
        standardTile("setAngleLevel", "device.setAngleLevel", width: 2, height: 2, canChangeIcon: true) {
            state "bright", label: 'Bright', action: "setFanSpeed", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"dim"
            state "dim", label: 'Dim', action: "setFanNatural", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"off"
            state "off", label: 'Off', action: "setAngleLevel", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"bright"
        } 
        standardTile("setMoveRight", "device.setMoveRight", width: 2, height: 2, canChangeIcon: true) {
            state "bright", label: 'Bright', action: "setMoveRight", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"dim"
            state "dim", label: 'Dim', action: "setMoveLeft", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"off"
            state "off", label: 'Off', action: "setAngleOff", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"bright"
        } 

   	main (["switch2"])
	details(["switch", "temp_label", "humi_label", "low_label", "medium_label", "high_label", "strong_label", 
    "temperature", "humidity", "speed1", "speed2", "speed3", "speed4", 
    "mode5", "mode6", 
    "buzzer_label", "led_label",
    "buzzer", "ledBrightness", "setAngleLevel","setMoveRight"
    ])

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
    case "relativeHumidity":
    	sendEvent(name:"humidity", value: params.data + "%")
    	break;
    case "power":
    	sendEvent(name:"switch", value: (params.data == "true" ? "on" : "off"))
    	break;
    case "temperature":
        sendEvent(name:"temperature", value: params.data)
    	break;
    }
    
    def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now)
}

def setFanSpeed(speed){
	log.debug "setFanSpeed >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "fanSpeed",
        "data": speed
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setFanNatural(speed){
	log.debug "setFanNatural >> ${state.id}"
    speed =15
    def body = [
        "id": state.id,
        "cmd": "fanNatural",
        "data": speed
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setAngleLevel(level){
	log.debug "setFanNatural >> ${state.id}"
    level = 30
    def body = [
        "id": state.id,
        "cmd": "angleLevel",
        "data": level
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setAngleOn(){
	log.debug "setAngleOn >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "angle",
        "data": "on"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setAngleOff(){
	log.debug "setAngleOff >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "angle",
        "data": "off"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setMoveLeft(){
	log.debug "setMoveLeft >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "moveDirection",
        "data": "Left"
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def setMoveRight(){
	log.debug "setMoveRight >> ${state.id}"
    def body = [
        "id": state.id,
        "cmd": "moveDirection",
        "data": "Right"
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
        "data": "dim"
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