// 挂到全局，方便页面直接用
// 注册
function registerApi(userInfo){
  return request.post("/register", userInfo)
}

// 登录
function loginApi(userInfo){
  return request.post("/login", userInfo)
}
