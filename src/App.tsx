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
import EventSource, { MessageEvent } from "react-native-sse";
import { ErrorEvent } from "ws";
import SmsModule from "./sms-module";

interface SMSData {
  phoneNumber: string;
  body: string;
  sentId: number;
}

function App(): JSX.Element {
  const [sse, setSse] = useState<EventSource<string>>();
  const [ip, setIp] = useState("");

  function connect() {
    show("connecting");

    const source = new EventSource<string>(`http://${ip}/app-gw`);

    source.addEventListener("open", () => {
      show("connected successfully");
      setSse(source);
    });

    source.addEventListener("message", (ev) => {
      const m = ev as MessageEvent;
      onMessage(m);
    });

    source.addEventListener("error", (ev) => {
      const err = ev as ErrorEvent;
      show(err.message);
      console.log(err.message);
      console.log(err.type);
    });

    source.addEventListener("close", onClose);
  }

  function onMessage(m: MessageEvent) {
    const sms = JSON.parse(m.data!) as SMSData;
    console.log(sms);
    SmsModule.send(sms.phoneNumber, sms.body);
  }

  function onClose() {
    setSse(undefined);
    show("disconnected");
  }

  function connected() {
    return Boolean(sse);
  }

  function toggle() {
    connected() ? sse?.close() : connect();
  }

  function show(message: string) {
    ToastAndroid.show(message, ToastAndroid.SHORT);
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar />
      <TextInput
        style={styles.input}
        onChangeText={(text) => setIp(text)}
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
