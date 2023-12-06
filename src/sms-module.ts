import { NativeModules } from "react-native";

interface SmsModule {
  send: (phoneNumber: string, message: string) => void;
}

const { SmsModule } = NativeModules;

export default SmsModule as SmsModule;
