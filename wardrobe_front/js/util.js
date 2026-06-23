// 通用工具函数存放处，后续拓展表单校验、本地存储等
export function storageSet(key, val) {
  localStorage.setItem(key, JSON.stringify(val))
}
export function storageGet(key) {
  const str = localStorage.getItem(key)
  return str ? JSON.parse(str) : null
}