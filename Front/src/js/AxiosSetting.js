import axios from "axios";
const instance = axios.create({
  baseURL: "http://ec2-43-201-47-190.ap-northeast-2.compute.amazonaws.com:8081",
});

export default instance;
