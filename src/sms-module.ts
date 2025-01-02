import { NativeModules } from "react-native";

interface SmsModule {
  send: (phoneNumber: string, message: string) => void;
  getPhoneNumber: () => Promise<string>
}

const { SmsModule } = NativeModules;

export default SmsModule as SmsModule;
