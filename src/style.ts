import {StyleSheet} from "react-native"

const style = StyleSheet.create({
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
    margin: 6,
    borderWidth: 1,
    width: 300,
    borderRadius: 5,
    paddingHorizontal: 10,
    paddingVertical: 5,
    fontSize: 20,
  },
  button: {
    marginTop: 5,
    backgroundColor: "#49f",
    height: 40,
    width: 150,
    borderRadius: 5,
    alignItems: "center",
    justifyContent: "center",
  },
  title: {
    fontSize: 50,
    marginBottom: 20,
    fontWeight: "500",
  },
});

export default style;