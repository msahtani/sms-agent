import { useEffect, useState } from "react";
import { ToastAndroid } from "react-native";
import EventSource, { MessageEvent } from "react-native-sse";
import SmsModule from "./sms-module";


export default function UseSse(onMessage: (m: MessageEvent) => void) 
  : [
    React.Dispatch<React.SetStateAction<string>>,
    () => boolean ,
    () => void
  ]{

  const [sse, setSse] = useState<EventSource<string>>();
  const [ip, setIp] = useState("209.38.170.140");

  let phoneNumber = "0704261627"


  // useEffect(() => {
  
  //   // Example: Fetching data (or doing any other side-effect)
  //   SmsModule.getPhoneNumber().then(
  //     result => {
  //       phoneNumber = result.match(/\d+/)![0]
  //       //console.log(phoneNumber)
  //     }
  //   ).catch(console.error)

  //   // Optional: Cleanup (if needed when the component unmounts)
  //   return () => {
  //     //console.log('Component unmounted');
  //   };
  // }, []);

  function connect() {
    show("connecting");

    const source = new EventSource<string>(
      `http://${ip}/app-gw?phoneNumber=${phoneNumber}`
    );

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

  return [setIp, connected, toggle]
}