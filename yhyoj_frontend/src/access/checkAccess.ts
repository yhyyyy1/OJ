import AccessEnum from "@/access/accessEnum";

/**
 * 检查权限（判断当前登录用户是否具有某个权限）
 * @param loginUser
 * @param needAccess
 * @return boolean 有无权限
 */
const checkAccess = (loginUser: any, needAccess = AccessEnum.NOT_LOGIN) => {
  //获取当前登录用户具有的权限，如果没有loginUser，默认未登录
  const loginUserAccess = loginUser?.userRole ?? AccessEnum.NOT_LOGIN;
  if (needAccess === AccessEnum.NOT_LOGIN) {
    return true;
  }
  //如果需要用户登录才能访问
  if (needAccess === AccessEnum.USER) {
    // 只要登录就可以了
    if (loginUserAccess === AccessEnum.NOT_LOGIN) return false;
  }

  if (needAccess === AccessEnum.ADMIN) {
    if (loginUserAccess !== AccessEnum.ADMIN) {
      return false;
    }
  }
  return true;
  //影响不大，属于少写代码了
};

export default checkAccess;
