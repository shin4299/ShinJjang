/**
 *  Xiaomi Power Strip (v.0.0.1)
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
	definition (name: "Xiaomi Power Strip", namespace: "fison67", author: "fison67") {
        capability "Actuator"
        capability "Switch"
        capability "Power Meter"
        capability "Energy Meter"
        capability "Configuration"
        capability "Refresh"
        capability "Sensor"
        capability "Outlet"
        
        attribute "Volt", "string"
        attribute "temp", "string"
        attribute "lastCheckin", "Date"
        
	}

	simulator { }

	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"https://postfiles.pstatic.net/MjAxODA0MDJfMTIz/MDAxNTIyNjcwODg2NDU4.YFzjMm5241sq8gebQ43cVk0BHe6ZGyzQltaOl6r-5cQg.ELNWg-YHD4VNfX0HvjO6NRSwHvH2VLnNs1OsQd-mC1kg.PNG.shin4299/tap_main_on.png?type=w3", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"https://postfiles.pstatic.net/MjAxODA0MDJfMjM5/MDAxNTIyNjcwODg2MjYw.cLth3MyXtofONl8yw-xQSEObfw4kd2VONYmR0xlos_wg.DWQ3z6Di7gAp4pKPXQrZXlMGlbXcrupzPCxUEAzoJZIg.PNG.shin4299/tap_main_off.png?type=w3", backgroundColor:"#ffffff", nextState:"turningOn"
                
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"https://postfiles.pstatic.net/MjAxODA0MDJfMjM5/MDAxNTIyNjcwODg2MjYw.cLth3MyXtofONl8yw-xQSEObfw4kd2VONYmR0xlos_wg.DWQ3z6Di7gAp4pKPXQrZXlMGlbXcrupzPCxUEAzoJZIg.PNG.shin4299/tap_main_off.png?type=w3", backgroundColor:"#00a0dc", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"https://postfiles.pstatic.net/MjAxODA0MDJfMTIz/MDAxNTIyNjcwODg2NDU4.YFzjMm5241sq8gebQ43cVk0BHe6ZGyzQltaOl6r-5cQg.ELNWg-YHD4VNfX0HvjO6NRSwHvH2VLnNs1OsQd-mC1kg.PNG.shin4299/tap_main_on.png?type=w3", backgroundColor:"#ffffff", nextState:"turningOn"
			}
            
            tileAttribute("device.power", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Meter: ${currentValue}\n ',icon: "st.Health & Wellness.health9")
            }
            tileAttribute("device.energy", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'                                 Energy: ${currentValue}KWh\n ',icon: "st.Health & Wellness.health9")
            }
            tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'\nUpdated: ${currentValue}',icon: "st.Health & Wellness.health9")
            }
		}
        valueTile("powerMeter", "device.power", width:2, height:2, inactiveLabel: false, decoration: "flat" ) {
        	state "powerMeter", label: 'Meter\n${currentValue}', action: "power", defaultState: true
		}
//        valueTile("powerVolt", "device.powerVolt", width:2, height:2, inactiveLabel: false, decoration: "flat" ) {
//        	state "volt", label: '현재전압\n${currentValue}', action: "volt", defaultState: true
//		}        
        valueTile("energyMeter", "device.energy", width:2, height:2, inactiveLabel: false, decoration: "flat" ) {
        	state "energyMeter", label: 'Energy\n${currentValue}KWh', action: "energy", defaultState: true
        }
        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label:"", action:"refresh", icon:"st.secondary.refresh"
        }
        main (["switch"])
        details(["switch", "powerMeter", "energyMeter", "refresh"])
        
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
    log.debug "${params.key} >> ${params.data}"
 
 	switch(params.key){
    case "power":
    	sendEvent(name:"switch", value: (params.data == "true" ? "on" : "off"))
    	break;
    case "powerLoad":
    	sendEvent(name:"power", value: params.data)
    	break;
    case "loadVoltage":
    	sendEvent(name:"power", value: params.data)
    	break;
    case "powerConsumed":
		def para = "${params.data}"
		String data = para
		def st = data.replace("J","");
		def stf = Float.parseFloat(st)
		def powerc = Math.round(stf)/1000
    	sendEvent(name:"energyMeter", value: powerc)
    	break;
    }
    
    updateLastTime()
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

def updateLastTime(){
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

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg
    try {
        msg = parseLanMessage(hubResponse.description)
		def jsonObj = new JsonSlurper().parseText(msg.body)

        sendEvent(name:"power", value: jsonObj.properties.powerLoad.value)
        sendEvent(name:"switch", value: (jsonObj.properties.power == true ? "on" : "off"))
        sendEvent(name:"energy", value: jsonObj.properties.powerConsumed.value/1000)
        
        
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
