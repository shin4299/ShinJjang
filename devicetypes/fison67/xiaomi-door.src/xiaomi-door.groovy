/**
 *  Xiaomi Door(v.0.0.1)
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
	definition (name: "Xiaomi Door", namespace: "fison67", author: "fison67") {
        capability "Configuration"
        capability "Sensor"
        capability "Contact Sensor"
        capability "Battery"
        capability "Health Check"

        attribute "lastCheckin", "Date"
        attribute "lastOpened", "String"

        command "resetClosed"
        command "resetOpen"
        command "refresh"
	}


	simulator {
	}

	tiles(scale: 2) {
		multiAttributeTile(name:"contact", type: "generic", width: 6, height: 4){
			tileAttribute ("device.contact", key: "PRIMARY_CONTROL") {
               	attributeState "open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13"
            	attributeState "closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00a0dc"
			}
            tileAttribute("device.lastOpened", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Last Opened: ${currentValue}',icon: "st.Health & Wellness.health9")
            }
		}
        
        valueTile("battery", "device.battery", decoration: "flat", width: 2, height: 2) {
            state "val", label:'${currentValue}', defaultState: true
        }
        standardTile("resetClosed", "device.resetClosed", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", action:"resetClosed", label:'Override Close', icon:"st.contact.contact.closed"
        }
        standardTile("resetOpen", "device.resetOpen", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", action:"resetOpen", label:'Override Open', icon:"st.contact.contact.open"
	}
        valueTile("lastcheckin", "device.lastCheckin", decoration: "flat", inactiveLabel: false, width: 4, height: 1) {
            state "default", label:'Last Checkin:\n${currentValue}'
        }
		
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
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)

 	switch(params.key){
    case "contact":
    	if (params.data == "true"){
    		sendEvent(name:"contact", value: "closed" )  
            }
        else {    
	    	sendEvent(name:"contact", value: "open" )
    	    sendEvent(name: "lastOpened", value: now, displayed: false)		
	        }
    case "batteryLevel":
    	sendEvent(name:"battery", value: params.data + "%")
    	break;
    }
    
    sendEvent(name: "lastCheckin", value: now)
}

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg
    try {
        msg = parseLanMessage(hubResponse.description)
		def jsonObj = new JsonSlurper().parseText(msg.body)
        setStatus(jsonObj.state)
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}

def updated() {
}

def refresh(){
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

def resetClosed() {
    sendEvent(name:"contact", value:"closed")
}

def resetOpen() {
    def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name:"contact", value:"open")
    sendEvent(name: "lastOpened", value: now, displayed: false)
}