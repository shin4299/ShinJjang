/**
 *  Simulated Alarm
 *
 *  Copyright 2014 SmartThings
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
    definition (name: "SecuritySystem2", namespace: "ShinJjang", author: "ShinJjang") {
		capability "Sensor"

        attribute "securityStatus", "enum", ["stay", "away", "off", "night", "alarm_active"]        

        command "securityStatus"
        command "away"
        command "off"
        command "night"
        command "stay"
        command "alarm_active"

	}

//	simulator {
		// reply messages
//		["strobe","siren","both","off"].each {
//			reply "$it": "alarm:$it"
//		}
//	}

	tiles {
        multiAttributeTile(name: "securityStatus", type: "generic", width: 6, height: 4) {
            tileAttribute("device.securityStatus", key: "PRIMARY_CONTROL") {
                attributeState("off", label:'보안해제', action:'off', icon:"st.locks.lock.unlocked", backgroundColor:"#9eacb6")
                attributeState("away", label:'외출모드', action:'away', icon:"st.Home.home3", backgroundColor:"#9fcc9c")
                attributeState("night", label:'취침모드', action:'night', icon:"st.Bedroom.bedroom11", backgroundColor:"#9c9fcc")
                attributeState("stay", label:'재실모드', action:'stay', icon:"st.nest.nest-home", backgroundColor:"#cc9c9f")
                attributeState("alarm_active", label:'경보모드', action:'alarm_active', icon:"st.security.alarm.alarm", backgroundColor:"b22525")
            }
        }

		standardTile("away", "device.status", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'외출모드', action:"away", icon:"st.Home.home3", backgroundColor:"#7f9c7d"
		}
		standardTile("night", "device.status", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'취침모드', action:"night", icon:"st.Bedroom.bedroom11", backgroundColor:"#7b7c98"
		}       
		standardTile("stay", "device.status", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'재실모드', action:"stay", icon:"st.nest.nest-home", backgroundColor:"#a98d8e"
		}       
		standardTile("off", "device.status", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'보안해제', action:"off", icon:"st.locks.lock.unlocked", backgroundColor:"#727d85"
		}
		standardTile("alarm_active", "device.status", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'경보모드', action:"alarm_active", icon:"st.security.alarm.alarm", backgroundColor:"#b22525"
		}

		main "securityStatus"
		details(["securityStatus","away","stay","night","off","alarm_active"])
	}
}

def away() {
	sendEvent(name: "securityStatus", value: "away")
}

def night() {
	sendEvent(name: "securityStatus", value: "night")
}

def stay() {
	sendEvent(name: "securityStatus", value: "stay")
}

def off() {
	sendEvent(name: "securityStatus", value: "off")
}

def alarm_active() {
	sendEvent(name: "securityStatus", value: "alarm_active")
}

// Parse incoming device messages to generate events
def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}