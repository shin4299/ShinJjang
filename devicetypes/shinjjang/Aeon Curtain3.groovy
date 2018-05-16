/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
definition (name: "Aeon Motor Controller x", namespace: "x", author: "x") {
capability "Refresh"
capability "Actuator"
capability "Switch"
capability "Switch Level"
capability "Window Shade"
    command "up"
    command "down"
    command "stop"
    
    fingerprint deviceId: "0x1107", inClusters: "0x25 0x26 0x70 0x85 0x72 0x86 0xEF 0x82"
    
}
	tiles {
    standardTile("windowShade", "device.windowShade", width: 3, height:2, canChangeIcon: true) {
        state ("default", label: 'STOPPED', icon:"st.Transportation.transportation13", backgroundColor: "#79b821")
        state("up", label:'UP', icon:"st.doors.garage.garage-opening", backgroundColor:"#53a7c0")
        state("down", label:'Down', icon:"st.doors.garage.garage-closing",  backgroundColor:"#ff0d00")
        state("stopUp", label:"STOPPED", icon:"st.Transportation.transportation13", backgroundColor:"#79b821")
        state("stopDn", label:"STOPPED", icon:"st.Transportation.transportation13",  backgroundColor:"#79b821")
   
    }
             
    standardTile("up", "device.switch",decoration: "flat") {
        state("default",label: "Up", action: "up", icon:"http://cdn.device-icons.smartthings.com/thermostat/thermostat-up@2x.png")
        
    }
    
    standardTile("down", "device.switch", decoration: "flat") {
        state ("default", label: "Down", action: "down", icon:"http://cdn.device-icons.smartthings.com/thermostat/thermostat-down@2x.png")
        
    }
    
   standardTile("stop", "device.switch",decoration: "flat") {
         state("default", label:"", action: "stop", icon:"http://cdn.device-icons.smartthings.com/sonos/stop-btn@2x.png")
    }
    
   
    standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
        state ("default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh")
    }
}
main(["windowShade"])
details([ "windowShade", "up","down","stop","refresh",])
}
// parse events into attributes
def parse(String description) {
def result = []
def cmd = zwave.parse(description,[0x20: 1, 0x26: 1])
if (cmd) {
result = zwaveEvent(cmd)
log.debug "{$description} parsed to {$result}"
} else {
	log.debug("Couldn’t zwave.parse {$description}")
}
return result
}
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
def result = []
if (!state.stp){
if(cmd.value == 0) {
result << createEvent(name: "windowShade", value: "down")
}else if(cmd.value == 255 || cmd.value == 99) {
result << createEvent(name: "windowShade", value: "up")
}}else {
def stopVal = state.up ? "stopUp" : "stopDn"
result << createEvent(name: "windowShade", value: stopVal)
}
return result
}
def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
def result = []
if (state.stp==false){
if(cmd.value == 0) {
result << createEvent(name: "windowShade", value: "down")
}else if(cmd.value == 255 || cmd.value == 99) {
result << createEvent(name: "windowShade", value: "up")
}}else {
def stopVal = state.up ? "stopUp" : "stopDn"
result << createEvent(name: "windowShade", value: stopVal)
}
return result
}
def refresh() {
delayBetween([
zwave.switchMultilevelV1.switchMultilevelGet().format(),
], 2000)
}
def up() {
state.up = true
state.stp=false
delayBetween([
zwave.basicV1.basicSet(value: 0xFF).format(),
zwave.switchMultilevelV1.switchMultilevelGet().format()
], 2000)
}
def down() {
state.up = false
state.stp=false
delayBetween([
zwave.basicV1.basicSet(value: 0x00).format(),
zwave.switchMultilevelV1.switchMultilevelGet().format()
], 2000)
}
def stop() {
state.stp=true
delayBetween([
zwave.switchMultilevelV1.switchMultilevelStopLevelChange().format(),
zwave.switchMultilevelV1.switchMultilevelGet().format(),
], 2000)
}
def on() {
state.up = true
state.stp=false
delayBetween([
zwave.basicV1.basicSet(value: 0xFF).format(),
zwave.switchMultilevelV1.switchMultilevelGet().format()
], 2000)
}
def off() {
state.up = false
state.stp=false
delayBetween([
zwave.basicV1.basicSet(value: 0x00).format(),
zwave.switchMultilevelv1.switchMultilevelGet().format()
], 2000)
}
