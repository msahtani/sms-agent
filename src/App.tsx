import React, { useState } from "react";
import {
  Pressable,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  ToastAndroid,
} from "react-native";
import { MessageEvent } from "react-native-sse";
import SmsModule from "./sms-module";
import UseSse from "./UseSse";

interface SMSData {
  phoneNumber: string;
  message: string;
  sentId?: number;
}

function App(): JSX.Element {

  
  
  // ToastAndroid.show(
  //   SmsModule.getPhoneNumber()
  //   , ToastAndroid.LONG
  // )
  
  const [setIp, connected, toggle] = UseSse(onMessage)

  function onMessage(m: MessageEvent) {
    if (!m.data) return
    const sms = JSON.parse(m.data) as SMSData;
    console.log(sms);
    SmsModule.send(sms.phoneNumber, sms.message);
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar />
      <TextInput
        style={styles.input}
        onChangeText={(text: string) => setIp(text)}
        placeholder="enter your IP address"
      />
      <Pressable style={styles.button} onPress={toggle}>
        <Text style={styles.text}>
          {connected() ? "disconnect" : "connect"}
        </Text>
      </Pressable>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    gap: 0,
    alignItems: "center",
    justifyContent: "center",
  },
  text: {
    fontSize: 25,
    color: "white",
    margin: "auto",
  },
  input: {
    margin: 12,
    borderWidth: 1,
    width: 300,
    borderRadius: 5,
    paddingHorizontal: 10,
    paddingVertical: 5,
    fontSize: 24,
  },
  button: {
    backgroundColor: "#49f",
    height: 40,
    width: 150,
    borderRadius: 5,
    alignItems: "center",
    justifyContent: "center",
  },
});

export default App;
