// 统一封装axios，挂到全局 request
var request = axios.create({
  baseURL: "http://localhost:8080/wardrobe_back",
  headers: {
    "Content-Type": "application/json"
  }
})

// 请求拦截：自动带 JWT token
request.interceptors.request.use(function(config){
  var token = localStorage.getItem("token");
  if (token) {
    config.headers["token"] = token;
  }
  return config;
}, function(err){
  return Promise.reject(err);
})

// 统一响应拦截
request.interceptors.response.use(function(res){
  return res.data
}, function(err){
  alert("网络请求失败，请检查后端服务")
  return Promise.reject(err)
})
