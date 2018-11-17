/**
 *  ESP Easy DTH (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 
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
import groovy.transform.Field


metadata {
	definition (name: "ESP relay 1ch", namespace: "ShinJjang", author: "ShinJjang", vid: "generic-switch") {
		capability "Actuator"
		capability "Switch"
        capability "Sensor"
        capability "Refresh"
        
        attribute "mode", "enum", ["mist", "rest", "off"]        
        
        attribute "lastCheckinDate", "date"
        
        command "setData"
        command "refresh"
        command "timerLoop"
		command	"checkNewDay"
        command "sprinkler"


	}


	simulator {
	}
    preferences {
		input "url", "text", title: "ESP IP주소", description: "로컬IP 주소를 입력", required: true
		input "gpio", "enum", title: "GPIO Pin", description: "릴레이 배당 GPIO Pin", defaultValue: 12, options:[9: "GPIO 9(D11)", 10 : "GPIO 10(D12)", 12: "GPIO 12(D6)", 13: "GPIO 13(D7)", 14: "GPIO 14(D5)", 15: "GPIO 15(D8)", 16: "GPIO 16(D0)"], displayDuringSetup: true
		input "AutoOff", "number", title: "자동 꺼짐 시간(초)", description: "ON 후 자동꺼짐 시간설정(초)", defaultValue: 0
        input name: "imoff", type: "bool", title: "즉시 꺼짐", description: "활성화시 켜지면 바로 꺼짐"
    }

	tiles {
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", backgroundColor:"#00a0dc", icon: "st.switches.switch.on", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", backgroundColor:"#ffffff", icon: "st.switches.switch.off", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", backgroundColor:"#00a0dc", icon: "st.switches.switch.off", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", backgroundColor:"#ffffff", icon: "st.switches.switch.on", nextState:"turningOn"
			}
		}    
		main "switch"
		details "switch"
	}

}


def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
    log.debug "URL >> ${url}"
	state.address = url
    state.gpiopin = gpio
    state.autooff = AutoOff as int
    }

def on(){
   relayOn()
}

def auto(){
}
def off(){
	relayOff()
}

def relayOn() {
    try{
        def options = [
            "method": "GET",
            "path": "/control?cmd=gpio,${state.gpiopin},0",
            "headers": [
                "HOST": state.address + ":80",
                "Content-Type": "application/json"
            ]
        ]
        def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: callback])
        sendHubCommand(myhubAction)
    }catch(e){
    	log.error "Error!!! ${e}"
    }
    sendEvent(name: "switch", value: "on")
    
    if (imoff){
   		relayOff()
	} else {
    	if (state.autooff >= 1){
   			runIn(AutoOff, relayOff)
  		 }
   }
}
def relayOff() {
    try{
        def options = [
            "method": "GET",
            "path": "/control?cmd=gpio,${state.gpiopin},1",
            "headers": [
                "HOST": state.address + ":80",
                "Content-Type": "application/json"
            ]
        ]
        def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: callback])
        sendHubCommand(myhubAction)
    }catch(e){
    	log.error "Error!!! ${e}"
    }
    sendEvent(name: "switch", value: "off")
}


