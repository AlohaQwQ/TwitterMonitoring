package com.web3.twitter;

import org.springframework.stereotype.Component;

@Component
public class GamesXiaomiRobotUtils {}
//public class GamesXiaomiRobotUtils {
//
//    @Autowired
//    private ISysDictTypeService dictTypeService;
//
//    @Autowired
//    private GamesMapper gamesMapper;
//
//    @Autowired
//    private GameTacticsMapper gameTacticsMapper;
//
//    @Autowired
//    private GameTacticsAdXiaomiMapper gameTacticsAdXiaomiMapper;
//
//    @Autowired
//    private GameTacticsExcludeMapper gameTacticsExcludeMapper;
//
//    @Autowired
//    private ReportGameDayMapper reportGameDayMapper;
//
//    @Autowired
//    private ReportAdMediaDayMapper reportAdMediaDayMapper;
//
//    @Autowired
//    private UserActiveLogMapper userActiveLogMapper;
//
//    @Autowired
//    private IUserActiveLogService userActiveLogService;
//
//    @Autowired
//    private ConfigDynamicGameTacticsMapper configDynamicGameTacticsMapper;
//
//    @Autowired
//    private GameRobotAccountConfigMapper gameRobotAccountConfigMapper;
//
//    private final RestTemplate restTemplate;
//
//    //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//    private final Integer GAME_ONLINE_PLATFORM_TYPE = 2;
//
//    private final String GAME_PLATFORM_AD_TYPE = "game_platform_ad_type";
//
//    //报告类型-应用报告/广告位报告/广告位类型报告
//    private final String ROBOT_REPORT_GAME_AD_TYPE_MEDIA = "mediaId";
//
//    private final String ROBOT_REPORT_GAME_AD_TYPE_POSITION_ID = "positionId";
//
//    private final String ROBOT_REPORT_GAME_AD_TYPE_POSITION_TYPE = "positionType";
//
//    private final Integer ROBOT_CATCH_PAGE_NUM = 8;
//
//    //活动日志表前缀
//    private final String TABLE_NAME_PREFIX = "user_active_log_";
//    //报告数据表前缀
//    private final String TABLE_NAME_REPORT_GAME_DAY_PREFIX = "report_game_day_";
//
//    private final String TABLE_NAME_REPORT_AD_DAY_PREFIX = "report_ad_media_day_";
//
//    //开放平台分析报告类型-新增用户/活跃用户/新增用户留存/活跃用户留存
//    private final String ROBOT_REPORT_GAME_DATA_TYPE_USER = "user";
//
//    private final String ROBOT_REPORT_GAME_DATA_TYPE_BEHAVIOR = "behavior_retention_analysis";
//
//    //应用报告数据
//    private static final String REPORT_GAME_DATA_URL = "https://dev.mi.com/sspsettle/api/data/stat/detail?userId=2895450891";
//    //应用报告收入数据
//    private static final String REPORT_GAME_TREND_DATA_URL = "https://dev.mi.com/sspsettle/api/data/stat/trend?userId=2895450891";
//
//    //留存数据
//    private static final String DATA_REPORT_RETENTION_URL = "https://open.oppomobile.com/resource/data/retention-list";
//
//    public GamesXiaomiRobotUtils(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    /**
//     * 登录获取cookie
//     *
//     * @return
//     */
//    public List<String> getCookies() {
//        try {
//            //查询启用的账号配置
//            GameRobotAccountConfig select = new GameRobotAccountConfig();
//            select.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);
//            select.setEnable(1);
//            List<GameRobotAccountConfig> gameRobotAccountConfigList =
//                    gameRobotAccountConfigMapper.selectGameRobotAccountConfigList(select);
//            List<String> cookiesList = new ArrayList<>();
//            if(gameRobotAccountConfigList!=null && !gameRobotAccountConfigList.isEmpty()){
//                for (GameRobotAccountConfig gameRobotAccountConfig : gameRobotAccountConfigList) {
//                    // 获取登录响应的 Cookie
//                    cookiesList.add(gameRobotAccountConfig.getConfigCookie());
//                }
//            } else {
//                LogUtils.error("Xiaomi模拟登录", "获取账号配置表异常");
//            }
//            // 返回 Cookies
//            return cookiesList;
//        } catch (Exception e) {
//            LogUtils.error("Xiaomi模拟登录", "模拟登录异常" + e);
//            return null;
//        }
//    }
//
//    /**
//     * 获取Xiaomi游戏信息数据 https://dev.mi.com/distribute?userId=2895450891
//     */
//    public void getGameList(Integer pageNum) {
//        //1.获取cookies
//        List<String> cookiesList = getCookies();
//        if (cookiesList == null || cookiesList.isEmpty()) {
//            LogUtils.error("定时查询xiaomi游戏信息列表", "获取Cookies失败");
//            return;
//        }
//
//        //多账号拉取数据
//        for (String cookies : cookiesList) {
//            if (!StringUtils.isEmpty(cookies)) {
//                //循环获取数据（这里直接填100000，数据肯定没这么多页，等获取到空了就结束循环）
//                for (int n = 0; n < pageNum; n++) {
//                    // 请求参数
//                    String cnName = "";
//                    Integer currentPageNum = n; // 当前页码（循环一直++）
//
//                    String url = String.format("https://dev.mi.com/uiueapi/myitems/myitemsV1?pageSize=10&searchingKeywords=%s&pageNum=%d&statusType=0&namespaceValue=0&mideveloper_ph=M6XZrnGAvuML88axdreDzQ==&userId=2895450891",
//                            cnName, currentPageNum);
//
//                    // 创建请求头并添加 cookies
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.APPLICATION_JSON);
//                    headers.add(HttpHeaders.COOKIE, cookies);
//
//                    // 创建请求实体
//                    HttpEntity<String> entity = new HttpEntity<>(headers);
//
//                    // 发送 GET 请求
//                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//                    // 处理响应数据
//                    if (response.getStatusCode() == HttpStatus.OK) {
//                        // 解析响应数据
//                        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//
//                        // String qq = "{\"code\":0,\"data\":{\"currentPageNum\":1,\"numPerPage\":10,\"pageCount\":11,\"recordCount\":104,\"data\":[{\"id\":608221,\"apppayId\":\"105763219\",\"typeId\":201,\"appType\":2,\"developerId\":408710,\"cnName\":\"难题粉碎者-休闲球球挑战\",\"versionName\":\"2.1.0\",\"versionCode\":\"40\",\"icon\":\"/developer/icon/20240614/906anarg/202406141255260ifjb.png\",\"apk\":\"/developer/apk/20240820/202408201934504tr0y.apk\",\"patchs\":\"\",\"size\":114908,\"packageName\":\"com.gzjw.ntfsz.vivo\",\"status\":2,\"updateDate\":1724809517000,\"onsaleDate\":1722851825000,\"saleStatus\":-1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"2.0.9\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":301,\"hasAppStoreOnline\":1,\"historyAppVO\":{\"appId\":608221,\"cnName\":\"难题粉碎者-休闲球球挑战\",\"versionName\":\"2.0.9\",\"versionCode\":\"35\",\"packageName\":\"com.gzjw.ntfsz.vivo\",\"saleStatus\":1},\"freezeStatus\":0,\"appStoreSale\":1},{\"id\":624777,\"apppayId\":\"105794909\",\"appType\":2,\"developerId\":408710,\"cnName\":\"小小像素物语-像素建筑世界\",\"icon\":\"/developer/icon/20240827/ilf9kvpl/202408271529209ieov.png\",\"packageName\":\"com.gzjw.xxxswy.vivo\",\"status\":-3,\"updateDate\":1724743945000,\"saleStatus\":-1,\"qualityStatus\":0,\"packageType\":1,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":101,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":0},{\"id\":598556,\"apppayId\":\"105747854\",\"typeId\":201,\"appType\":2,\"developerId\":408710,\"cnName\":\"我的虚拟生活-宝宝捉迷藏躲避\",\"versionName\":\"2.1.0\",\"versionCode\":\"40\",\"icon\":\"/developer/apk/icon/20240702/2024070214063954a0g.png\",\"apk\":\"/developer/apk/20240827/20240827151337049v9.apk\",\"patchs\":\"\",\"size\":112938,\"packageName\":\"com.gzjw.wdxnsh.vivo\",\"status\":3,\"updateDate\":1724742835000,\"onsaleDate\":1724752872000,\"saleStatus\":1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"2.1.0\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":501,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":1},{\"id\":624704,\"apppayId\":\"105794792\",\"appType\":2,\"developerId\":408710,\"cnName\":\"冲向终点3-生存爬坡赛车\",\"icon\":\"/developer/icon/20240827/gkdd39d4/202408271114105mtrs.png\",\"packageName\":\"com.gzjw.cxzd3.vivo\",\"status\":-3,\"updateDate\":1724728558000,\"saleStatus\":-1,\"qualityStatus\":0,\"packageType\":1,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":101,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":0},{\"id\":624587,\"apppayId\":\"105794618\",\"appType\":2,\"developerId\":408710,\"cnName\":\"车辆驾驶模拟器-警车驾驶模拟器\",\"icon\":\"/developer/icon/20240826/qlp9txoc/202408261823027msxc.png\",\"packageName\":\"com.gzjw.cljsmnq.vivo\",\"status\":-3,\"updateDate\":1724667930000,\"saleStatus\":-1,\"qualityStatus\":0,\"packageType\":1,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":101,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":0},{\"id\":611488,\"apppayId\":\"105769007\",\"typeId\":203,\"appType\":2,\"developerId\":408710,\"cnName\":\"特工亨特-狙击手枪战射击行动\",\"versionName\":\"1.0.3\",\"versionCode\":\"40\",\"icon\":\"/developer/icon/20240628/474km97y/20240628112337784xq.png\",\"apk\":\"/developer/apk/20240822/202408221826549s1lt.apk\",\"patchs\":\"\",\"size\":113601,\"packageName\":\"com.gzjw.tght.vivo\",\"status\":3,\"updateDate\":1724666543000,\"onsaleDate\":1724760789000,\"saleStatus\":1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"1.0.3\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":501,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":1},{\"id\":601303,\"apppayId\":\"105752069\",\"typeId\":204,\"appType\":2,\"developerId\":408710,\"cnName\":\"组装大师-放置英雄花园背包闯关\",\"versionName\":\"2.1.0\",\"versionCode\":\"40\",\"icon\":\"/developer/icon/20240605/4l818bpa/2024060514101497gq1.png\",\"apk\":\"/developer/apk/20240826/202408261108431lxtp.apk\",\"patchs\":\"\",\"size\":106398,\"packageName\":\"com.gzjw.zzds.vivo\",\"status\":3,\"updateDate\":1724641752000,\"onsaleDate\":1724760483000,\"saleStatus\":1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"2.1.0\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":501,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":1},{\"id\":597006,\"apppayId\":\"105740924\",\"typeId\":201,\"appType\":2,\"developerId\":408710,\"cnName\":\"玩具王国-休闲解压拆快递\",\"versionName\":\"2.1.1\",\"versionCode\":\"40\",\"icon\":\"/developer/apk/icon/20240701/2024070110041889d3h.png\",\"apk\":\"/developer/apk/20240823/202408231112427sh2b.apk\",\"patchs\":\"\",\"size\":100363,\"packageName\":\"com.gzjw.wjwg.vivo\",\"status\":3,\"updateDate\":1724639648000,\"onsaleDate\":1724724782000,\"saleStatus\":1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"2.1.1\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":501,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":1},{\"id\":615690,\"apppayId\":\"105780419\",\"typeId\":209,\"appType\":2,\"developerId\":408710,\"cnName\":\"养殖场大亨-海鲜加工厂\",\"versionName\":\"1.0.5\",\"versionCode\":\"40\",\"icon\":\"/developer/icon/20240724/iu3u4hqi/202407241304117zkrg.png\",\"apk\":\"/developer/apk/20240820/202408202102068i4ln.apk\",\"patchs\":\"\",\"size\":177380,\"packageName\":\"com.gzjw.yzcdh.vivo\",\"status\":3,\"updateDate\":1724400064000,\"onsaleDate\":1724466544000,\"saleStatus\":1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"1.0.5\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":501,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":1},{\"id\":603044,\"apppayId\":\"105754697\",\"typeId\":201,\"appType\":2,\"developerId\":408710,\"cnName\":\"进击吧火柴人-火柴人掘地闯关\",\"versionName\":\"2.1.0\",\"versionCode\":\"40\",\"icon\":\"/developer/icon/20240520/75oh9k3h/2024052016330227mv7.png\",\"apk\":\"/developer/apk/20240822/202408221711092ky3m.apk\",\"patchs\":\"\",\"size\":89182,\"packageName\":\"com.gzjw.jjbhcr.vivo\",\"status\":3,\"updateDate\":1724385297000,\"onsaleDate\":1724462942000,\"saleStatus\":1,\"qualityStatus\":2,\"packageType\":2,\"contractStatus\":2,\"cooperateType\":0,\"appVideo\":{},\"appTypeInfo\":{},\"appScreenshots\":[],\"needSignContract\":2,\"needSignTransport\":0,\"synDateFlag\":false,\"saleVersionName\":\"2.1.0\",\"payStatus\":1,\"evaluationStatus\":0,\"compositeStatus\":501,\"hasAppStoreOnline\":0,\"freezeStatus\":0,\"appStoreSale\":1}]}}";
//                        // JSONObject jsonObject = JSONObject.parseObject(qq);
//                        if (jsonObject == null) {
//                            LogUtils.info("定时查询xiaomi游戏信息列表:", "数据返回异常 | " + response);
//                            // 结束循环
//                            continue;
//                        }
//                        if (jsonObject.getJSONObject("data") == null) {
//                            LogUtils.info("定时查询xiaomi游戏信息列表:", "数据返回异常 | " + jsonObject);
//                            // 结束循环
//                            continue;
//                        }
//
//                        // 获取应用列表
//                        JSONArray appList = jsonObject.getJSONObject("data").getJSONArray("appItemList");
//
//                        //判断集合是否为空
//                        if (appList == null || appList.isEmpty()) {
//                            LogUtils.info("定时查询xiaomi游戏信息列表", "无更多数据");
//                            // 结束循环（结束的是最外层100000次的这个循环）
//                            break;
//                        }
//
//                        //循环处理数据
//                        for (int i = 0; i < appList.size(); i++) {
//                            //1.取出数据转成JSONObject对象
//                            JSONObject app = appList.getJSONObject(i);
//                            //System.out.println(app);
//
//                            String appId = app.getString("appId"); //渠道游戏ID
//                            //读取游戏详情接口
//                            String versionCode = getGameVersionCode(entity, appId);
//
//                            //3.先根据游戏包名查询一下数据库是否已经有这条数据
//                            String packageName = app.getString("packageName");
//                            Map<String, Object> params = new HashMap<>();
//                            params.put("packageName", packageName);
//                            params.put("onlinePlatformType", GAME_ONLINE_PLATFORM_TYPE);
//                            //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                            Games game = gamesMapper.selectGamesByPackageName(params);
//                            if (game == null) {
//                                //2.封装数据
//                                game = new Games();
//                                setGameDetails(game, app);
//                                //设置版本号
//                                game.setVersionCode(versionCode);
//                                //新游戏
//                                gamesMapper.insertGames(game);
//                                LogUtils.info("定时查询xiaomi游戏信息列表", "新游戏：" + game);
//                                //获取Xiaomi渠道的 策略屏蔽配置
//                                GameTacticsExclude gameTacticsExclude = new GameTacticsExclude();
//                                gameTacticsExclude.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);
//                                List<GameTacticsExclude> selectGameTacticsExcludeList = gameTacticsExcludeMapper.selectGameTacticsExcludeList(gameTacticsExclude);
//                                for (GameTacticsExclude exclude : selectGameTacticsExcludeList) {
//                                    //屏蔽城市不为空
//                                    if(!exclude.getTacticsMate().isEmpty()){
//                                        //新游戏默认加入策略屏蔽配置中
//                                        JSONArray jsonArray;
//                                        if(!exclude.getGameId().isEmpty()){
//                                            jsonArray = JSONArray.parseArray(exclude.getGameId());
//                                        } else {
//                                            jsonArray = new JSONArray();
//                                        }
//                                        jsonArray.add(game.getGameId());
//                                        exclude.setGameId(jsonArray.toString());
//                                        //更新屏蔽策略
//                                        gameTacticsExcludeMapper.updateGameTacticsExclude(exclude);
//                                    }
//                                }
//                            } else {
//                                setGameDetails(game, app);
//                                //设置版本号
//                                game.setVersionCode(versionCode);
//                                //更新游戏信息
//                                gamesMapper.updateGames(game);
//                            }
//
//
//                            //判断游戏广告策略配置
//                            //先根据游戏包名查询一下数据库是否已经有这条数据
//                            List<GameTactics> gameTacticsList = gameTacticsMapper.selectGameTacticsListByPackageName(game.getPackageName());
//                            //读取id为1的预设策略配置
//                            //GameTactics gameTacticsOrigin = gameTacticsMapper.selectGameTacticsById(1L);
//                            //查询对应渠道动态配置
//                            ConfigDynamicGameTactics select = new ConfigDynamicGameTactics();
//                            select.setOnlinePlatformType(Long.valueOf(GAME_ONLINE_PLATFORM_TYPE));
//                            ConfigDynamicGameTactics configDynamicGameTactics = configDynamicGameTacticsMapper.selectConfigDynamicGameTactics(select);
//                            //策略预填充游戏相关字段
//                            GameTactics gameTacticsNew = getGameTactics(game);
//
//                            if (gameTacticsList == null || gameTacticsList.isEmpty()) {
//                                //新游戏配置默认策略 沿用预设策略配置
//                                //setGameTacticsProperties(gameTacticsNew, gameTacticsOrigin);
//                                //沿用对应渠道预设策略配置
//                                if (configDynamicGameTactics != null) {
//                                    gameTacticsNew.setExpandField(configDynamicGameTactics.getDefaultValues());
//                                }
//                                gameTacticsMapper.insertGameTactics(gameTacticsNew);
//                                LogUtils.info("定时查询xiaomi游戏信息列表", "新游戏策略：" + gameTacticsNew.getGameName());
//                            } else {
//                                // 已有策略，判断版本
//                                // 未提交游戏，无旧版本并且已经新建过策略，版本信息为空
//                                // 校验提交游戏，有旧版本并且已经新建过策略，版本信息不为空
//                                if(game.getSubmitType()>0){
//                                    // 根据游戏包名和游戏版本名查询游戏策略广告配置
//                                    Map<String, Object> paramsPackage = new HashMap<>();
//                                    paramsPackage.put("versionCode", game.getVersionCode());
//                                    paramsPackage.put("packageName", game.getPackageName());
//                                    GameTactics gameTacticsVersion = gameTacticsMapper.selectGameTacticsByPackageNameVersion(paramsPackage);
//                                    //该版本没有配置策略
//                                    if (gameTacticsVersion == null) {
//                                        //延用上一个版本的策略并复制
//                                        GameTactics gameTacticsLast = gameTacticsList.stream()
//                                                .max(Comparator.comparing(GameTactics::getId))
//                                                .orElse(gameTacticsList.get(0));
//
//                                        //setGameTacticsProperties(gameTacticsNew, gameTacticsLast);
//                                        //沿用对应渠道预设策略配置
//                                        if (configDynamicGameTactics != null) {
//                                            gameTacticsNew.setExpandField(configDynamicGameTactics.getDefaultValues());
//                                        }
//
//                                        gameTacticsMapper.insertGameTactics(gameTacticsNew);
//                                        LogUtils.info("定时查询xiaomi游戏信息列表", "新游戏版本策略：" + gameTacticsNew.getGameName());
//
//                                        //延用上一个版本的策略广告列表信息并复制
//                                        GameTacticsAd ad = new GameTacticsAd();
//                                        ad.setGameId(gameTacticsLast.getGameId());
//                                        ad.setOnlinePlatformType(gameTacticsLast.getOnlinePlatformType());
//                                        ad.setVersionCode(gameTacticsLast.getVersionCode());
//                                        //查询上一个版本所有广告配置信息列表
//                                        List<GameTacticsAd> gameTacticsAdList = gameTacticsAdXiaomiMapper.selectGameTacticsAdXiaomiList(ad);
//                                        if (gameTacticsAdList != null && !gameTacticsAdList.isEmpty()) {
//                                            for (GameTacticsAd gameTacticsAd : gameTacticsAdList) {
//                                                // 处理每个 GameTactics 对象
//                                                GameTacticsAd gameTacticsAdVivoNew = new GameTacticsAd();
//                                                copyGameTacticsAdVivoProperties(gameTacticsAdVivoNew, gameTacticsAd);
//                                                // 设置新版本号
//                                                gameTacticsAdVivoNew.setVersionCode(gameTacticsNew.getVersionCode());
//                                                // 每个版本策略都新增该广告位配置
//                                                gameTacticsAdXiaomiMapper.insertGameTacticsAdXiaomi(gameTacticsAdVivoNew);
//                                            }
//                                        }
//                                        LogUtils.info("定时查询xiaomi游戏信息列表", "新游戏版本策略广告列表复制：" + gameTacticsNew.getGameName());
//                                    } else {
//                                        gameTacticsVersion.setSubmitType(game.getSubmitType());
//                                        //更新策略游戏审核状态
//                                        gameTacticsMapper.updateGameTactics(gameTacticsVersion);
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        LogUtils.info("定时查询xiaomi游戏信息列表", "请求失败：" +response.getStatusCode());
//                    }
//                }
//            }
//        }
//    }
//
//    //game bean
//    private void setGameDetails(Games game, JSONObject app) {
//        game.setGameName(app.getString("displayName")); //游戏名称
//        game.setGameId(app.getString("appId")); //渠道游戏ID
//        game.setGameIcon(StringUtils.isEmpty(app.getString("icon")) ? "https://open.oppomobile.com/oneoppo/static/images/app_default_icon.de382da.png"
//                : app.getString("icon")); //图标
//
//        game.setPackageName(app.getString("packageName")); //包名
//        game.setAppId(app.getString("appId")); //AppPayId
//        game.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//
//        //判断上架状态
//        game.setOnlineType(0); //上架状态(0:未上架, 1:已上架)
//        //Integer saleStatus = app.getInteger("saleStatus"); //上架状态（-1：未上架，1:已上架）
//        //if(saleStatus == 1){
//        //    game.setOnlineType(1);
//        //}
//        //判断提交版本状态
//        String statusType = app.getString("statusType"); //提交版本状态（1:已上架，2:被下架，3：未发布，41:自动化审核中，42:人工审核中，5:审核失败，6：定时上线）
//        if (statusType == null) {
//            game.setSubmitType(0); // 未提交
//        } else if (statusType.equals("3")) {
//            game.setSubmitType(0); // 未提交
//        } else if (statusType.equals("41")) {
//            game.setSubmitType(1); // 测试中
//        } else if (statusType.equals("42")) {
//            game.setSubmitType(2); // 审核中
//        } else if (statusType.equals("6")) {
//            game.setSubmitType(3); // 待发布
//        } else if (statusType.equals("1")) {
//            game.setSubmitType(4); // 已上架
//            game.setOnlineType(1);
//        } else if (statusType.equals("2")) {
//            game.setSubmitType(5); // 已下架
//        } else if (statusType.equals("5")) {
//            game.setSubmitType(6); // 审核不通过
//        } else {
//            game.setSubmitType(0); // 或者设置为其他默认值，若需要处理未定义情况
//        }
//        game.setVersionName(app.getString("versionName")); //版本名称
//        //game.setVersionCode(app.getString("versionCode")); //版本号
//        game.setCreateTime(app.getDate("createTime")); //创建时间
//        game.setOnlineTime(app.getDate("updateTime")); //上架时间
//        game.setUpdateTime(app.getDate("updateTime")); //更新时间
//
//        //game.setPublisherId(app.getString("developerId"));
//        //game.setCreateBy(app.getString("developerId"));
//    }
//
//    //set game info
//    private GameTactics getGameTactics(Games games) {
//        GameTactics gameTacticsNew = new GameTactics();
//        gameTacticsNew.setGameId(games.getGameId());//渠道游戏ID
//        gameTacticsNew.setGameName(games.getGameName()); //游戏名
//        gameTacticsNew.setPackageName(games.getPackageName()); //游戏包名
//        gameTacticsNew.setGameIcon(games.getGameIcon()); //游戏名
//        gameTacticsNew.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);
//        gameTacticsNew.setSubmitType(games.getSubmitType());
//        gameTacticsNew.setAppId(games.getAppId());
//        gameTacticsNew.setVersionName(games.getVersionName());
//        gameTacticsNew.setVersionCode(games.getVersionCode());
//        gameTacticsNew.setSdkVersionCode(games.getSdkVersionCode());
//        return gameTacticsNew;
//    }
//
//    private String getGameVersionCode(HttpEntity<String> entity, String appId) {
//        String url = String.format("https://dev.mi.com/uiueapi/app/detailsPage?namespaceValue=0&appId=%s&mideveloper_ph=M6XZrnGAvuML88axdreDzQ==&userId=2895450891",
//                appId);
//        // 发送 GET 请求
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        String versionCode = "0";
//        // 处理响应数据
//        if (response.getStatusCode() == HttpStatus.OK) {
//            // 解析响应数据
//            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//            // 获取应用列表
//            JSONObject data = jsonObject.getJSONObject("data");
//            if(data.containsKey("versionCode")){
//                versionCode = data.getString("versionCode"); //versionCode
//            }
//        }
//        return versionCode;
//    }
//
//    //copy bean
//    private void setGameTacticsProperties(GameTactics gameTactics, GameTactics source) {
//        //预设json 参数
//        gameTactics.setExpandField(source.getExpandField());
//    }
//
//    /**
//     * 查询广告位配置的信息 https://dev.mi.com/union/static/index.html?userId=2895450891#/advertise
//     */
//    public void getAdManage(Integer pageNum) {
//        // 获取cookies
//        List<String> cookiesList = getCookies();
//        if (cookiesList == null || cookiesList.isEmpty()) {
//            LogUtils.error("定时查询xiaomi广告位配置", "获取Cookies失败");
//            return;
//        }
//        List<SysDictData> gameAdTypeList = getAdType(GAME_PLATFORM_AD_TYPE);
//
//        //多账号拉取数据
//        for (String cookies : cookiesList) {
//            if (!StringUtils.isEmpty(cookies)) {
//                // 循环获取数据（这里直接填100000，数据肯定没这么多页，等获取到空了就结束循环）
//                for (int n = 1; n < pageNum; n++) {
//                    // 请求参数
//                    String publisherParam = "";//游戏检索条件，模糊查询，包名或者名称
//                    int pageIndex = n; // 当前页码（循环一直++）
//                    int pageSize = 20;
//
//                    // 构建请求URL, placementStatus=1 查询运行中的广告位
//                    String url = String.format("https://dev.mi.com/memberapi/placement/list?userId=2895450891&publisherParam=%s&placementStatus=1&pageNumber=%d&pageSize=50",
//                            publisherParam, pageIndex);
//
//                    // 创建请求头并添加 cookies
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.APPLICATION_JSON);
//                    headers.add(HttpHeaders.COOKIE, cookies);
//
//                    // 创建请求实体
//                    HttpEntity<String> entity = new HttpEntity<>(headers);
//
//                    // 发送 GET 请求
//                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//                    // 处理响应数据
//                    if (response.getStatusCode() == HttpStatus.OK) {
//                        // 解析响应数据
//                        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                        if (jsonObject == null) {
//                            LogUtils.info("定时查询xiaomi广告位配置:", "数据返回异常 | " + response);
//                            // 结束循环
//                            continue;
//                        }
//                        if (jsonObject.getJSONObject("data") == null) {
//                            LogUtils.info("定时查询xiaomi广告位配置:", "数据返回异常 | " +jsonObject);
//                            // 结束循环
//                            continue;
//                        }
//                        // 获取数据列表
//                        JSONArray dataList = jsonObject.getJSONObject("data").getJSONArray("list");
//
//                        // 判断集合是否为空
//                        if (dataList == null || dataList.isEmpty()) {
//                            LogUtils.info("定时查询xiaomi广告位配置", "无更多数据");
//                            // 结束循环（结束的是最外层100000次的这个循环）
//                            break;
//                        }
//
//                        // 循环处理数据集合
//                        for (int i = 0; i < dataList.size(); i++) {
//                            // 取出数据转成JSONObject对象
//                            JSONObject data = dataList.getJSONObject(i);
//                            // 打印数据或者进行其他处理
//                            //System.out.println(data);
//
//                            // 1.封装数据
//                            GameTacticsAd gameTacticsAdXiaomi = new GameTacticsAd();
//                            String packageName = data.getString("packageName"); // 包名
//                            //Integer isEnable = data.getInteger("isEnable"); //是否启用 0：启用 1：冻结
//                            String adPlaceName = data.getString("placementName"); // 广告位名称
//
//                            //if (isEnable == 1) {
//                                //跳过冻结策略
//                                //LogUtils.info("定时查询xiaomi广告位配置", "广告位配置已冻结跳过：" + data);
//                                //continue;
//                            //}
//                            //adPlaceName.contains("系统开屏")
//                            //if (adPlaceName.contains("盒子")) {
//                            //    LogUtils.info("定时查询xiaomi广告位配置", "广告位配置系统开屏/盒子广告跳过：" + data);
//                            //    continue;
//                            //}
//                            Map<String, Object> params = new HashMap<>();
//                            params.put("packageName", packageName);
//                            params.put("onlinePlatformType", GAME_ONLINE_PLATFORM_TYPE);
//                            //3.先根据游戏包名查询一下数据库是否已经有这个游戏
//                            //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                            Games game = gamesMapper.selectGamesByPackageName(params);
//                            if (game == null) {
//                                //跳过该条策略
//                                LogUtils.info("定时查询xiaomi广告位配置", "广告位配置写入失败，游戏匹配为空：" + data);
//                                continue;
//                            }
//                            gameTacticsAdXiaomi.setGameId(game.getGameId());//渠道游戏ID
//                            gameTacticsAdXiaomi.setGameName(game.getGameName());//游戏名称
//                            gameTacticsAdXiaomi.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);//游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                            gameTacticsAdXiaomi.setAdPlaceId(data.getString("placementId"));// 广告位ID
//
//                            gameTacticsAdXiaomi.setAdPlaceName(adPlaceName); // 广告位名称
//                            //无样式广告位类型 系统开屏/横幅/插屏
//                            //有样式广告位类型 原生横幅-上图下文/插屏全屏-左图右文
//                            String adPlaceTypeName = adPlaceName.split("-")[0];//以-分割，第一位为广告位类型
//                            adPlaceTypeName = adPlaceTypeName.replace(" ","");
//                            // 匹配标签  系统开屏:0 横幅:1 半插:2 原生模板:3 激励视频:4 失败:5 原生模板横幅:6 全插:7 原生自渲染:8 原生自渲染横幅:9
//                            if(adPlaceName.contains("全")){ //插屏-全屏竖版3 插屏-全屏3 插屏-全插2 插屏-竖版全屏3
//                                gameTacticsAdXiaomi.setAdPlaceType(7); // 全插:7
//                                gameTacticsAdXiaomi.setAdPointRate(20);
//                            } else if(adPlaceName.contains("半") || adPlaceName.contains("竖")){ //插屏-竖3 半插-4
//                                gameTacticsAdXiaomi.setAdPlaceType(2); // 半插:2
//                                gameTacticsAdXiaomi.setAdPointRate(20);
//                            } else if(adPlaceTypeName.equals("横幅")){
//                                gameTacticsAdXiaomi.setAdPlaceType(1); // 横幅:1
//                                gameTacticsAdXiaomi.setAdPointRate(10);
//                            } else if(adPlaceTypeName.contains("系统开屏")){
//                                gameTacticsAdXiaomi.setAdPlaceType(0); // 系统开屏:0
//                                gameTacticsAdXiaomi.setAdPointRate(12);
//                            } else if(adPlaceTypeName.contains("激励视频")){
//                                gameTacticsAdXiaomi.setAdPlaceType(4); //激励视频:4
//                                gameTacticsAdXiaomi.setAdPointRate(25);
//                            } else if(adPlaceTypeName.contains("原生模板横幅") || adPlaceTypeName.contains("原生横幅")){
//                                gameTacticsAdXiaomi.setAdPlaceType(6); // 原生模板横幅:6
//                                gameTacticsAdXiaomi.setAdPointRate(10);
//                            } else if(adPlaceTypeName.equals("原生") || adPlaceTypeName.equals("原生模板")){
//                                gameTacticsAdXiaomi.setAdPlaceType(3); // 原生模板:3
//                                gameTacticsAdXiaomi.setAdPointRate(10);
//                            } else {
//                                for (SysDictData sysDictData : gameAdTypeList) {
//                                    String dictLabel = sysDictData.getDictLabel();
//                                    // 匹配标签  系统开屏:0 横幅:1 插屏:2 原生插屏:3
//                                    if(adPlaceTypeName.equals(dictLabel)){
//                                        gameTacticsAdXiaomi.setAdPlaceType(Integer.valueOf(sysDictData.getDictValue())); // 设置广告类型
//                                        gameTacticsAdXiaomi.setAdPointRate(10);
//                                    }
//                                }
//                            }
//
//                            gameTacticsAdXiaomi.setAdPlaceWeight(1);
//                            //gameTacticsAdVivo.setAdPointRate(10);
//                            gameTacticsAdXiaomi.setAdEnable(1); //是否启用(0:否, 1:是)
//                            //读取id为1的预设策略配置
//                            GameTacticsAd gameTacticsAdOrigin = gameTacticsAdXiaomiMapper.selectGameTacticsAdXiaomiById(1L);
//                            //预填充策略广告扩展字段
//                            gameTacticsAdXiaomi.setExpandField(gameTacticsAdOrigin.getExpandField());
//
//                            gameTacticsAdXiaomi.setCreateTime(data.getDate("createTime")); //创建时间
//                            gameTacticsAdXiaomi.setUpdateTime(data.getDate("updateTime")); //更新时间
//                            //gameTacticsAdVivo.setCreateBy(data.getString("createBy"));
//
//                            // 根据游戏上架平台/游戏ID/游戏策略广告位ID查询游戏策略广告配置
//                            Map<String, Object> paramsAdByPlace = new HashMap<>();
//                            paramsAdByPlace.put("gameId", gameTacticsAdXiaomi.getGameId());
//                            paramsAdByPlace.put("onlinePlatformType", gameTacticsAdXiaomi.getOnlinePlatformType());
//                            paramsAdByPlace.put("adPlaceId", gameTacticsAdXiaomi.getAdPlaceId());
//                            // 多版本广告位配置
//                            List<GameTacticsAd> gameTacticsAdList = gameTacticsAdXiaomiMapper.selectGameTacticsAdByPlace(paramsAdByPlace);
//                            // 没有配置过该条广告位信息
//                            if (gameTacticsAdList == null || gameTacticsAdList.isEmpty()) {
//                                // 根据包名查询配置过的所有版本策略
//                                List<GameTactics> gameTacticsList = gameTacticsMapper.selectGameTacticsListByPackageName(game.getPackageName());
//                                gameTacticsList.forEach(gameTactics -> {
//                                    // 处理每个 GameTactics 对象
//                                    GameTacticsAd gameTacticsAdVivoNew = new GameTacticsAd();
//                                    copyGameTacticsAdVivoProperties(gameTacticsAdVivoNew, gameTacticsAdXiaomi);
//                                    // 设置版本号
//                                    gameTacticsAdVivoNew.setVersionCode(gameTactics.getVersionCode());
//                                    // 每个版本策略都新增该广告位配置
//                                    gameTacticsAdXiaomiMapper.insertGameTacticsAdXiaomi(gameTacticsAdVivoNew);
//                                    LogUtils.info("定时查询xiaomi广告位配置", "新游戏广告策略：" + gameTacticsAdVivoNew.getAdPlaceName());
//                                });
//                            } else {
//                                LogUtils.info("定时查询xiaomi广告位配置", "广告位配置信息已存在，无需再次插入：" + data);
//                            }
//                        }
//                    } else {
//                        LogUtils.error("定时查询xiaomi广告位配置", "请求失败：" +response.getStatusCode());
//                    }
//                }
//            }
//        }
//    }
//
//    //copy bean
//    private void copyGameTacticsAdVivoProperties(GameTacticsAd target, GameTacticsAd source) {
//        target.setGameId(source.getGameId());
//        target.setGameName(source.getGameName());
//        target.setOnlinePlatformType(source.getOnlinePlatformType());
//        target.setAdEnable(source.getAdEnable());
//        target.setAdPlaceId(source.getAdPlaceId());
//        target.setAdPlaceName(source.getAdPlaceName());
//        target.setAdPlaceType(source.getAdPlaceType());
//        target.setAdPlaceWeight(source.getAdPlaceWeight());
//        target.setAdPointRate(source.getAdPointRate());
//        target.setExpandField(source.getExpandField());
//        target.setCreateTime(source.getCreateTime());
//        target.setUpdateTime(source.getUpdateTime());
//        target.setCreateBy(source.getCreateBy());
//    }
//
//    /**
//     * 查询系统配置字典
//     * @param dictType
//     * @return
//     */
//    public List<SysDictData> getAdType(String dictType) {
//        return dictTypeService.selectDictDataByType(dictType);
//    }
//
//    /**
//     * 获取应用报告数据
//     * https://dev.mi.com/union/static/index.html?userId=2895450891#/analytics
//     */
//    public void getReportGameData() {
//        // 获取cookies
//        List<String> cookiesList = getCookies();
//        if (cookiesList == null || cookiesList.isEmpty()) {
//            LogUtils.info("获取 Cookies 失败");
//            return;
//        }
//
//        //1.先去库里把所有游戏查询出来
//        Games games = new Games();
//        //games.setGameId(611192L);
//        games.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);
//        List<Games> gamesList = gamesMapper.selectGamesList(games);
//        List<GamesSimpleVo> gamesSimpleList = new ArrayList<>();
//        for (Games game : gamesList) {
//            GamesSimpleVo simpleVo = new GamesSimpleVo(game.getGameId(),game.getPackageName(),
//                    game.getGameName(), game.getVersionName(), game.getVersionCode());
//            gamesSimpleList.add(simpleVo);
//        }
//
//        //创建报告相关表
//        //匹配当前时间对应的表名, report_vivo_game_day_202410, 根据月份拆分表
//        Date nowDate = DateUtils.parseDate(DateUtils.getDate());
//        //Date nowDateMM = DateUtils.parseDate(DateUtils.getDateMM());
//        //匹配昨天时间对应的表名
//        Date nowDateMM = DateUtils.parseDate(DateHandleUtil.getDataTimeYYMM(1));
//
//        String tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + DateUtils.dateTimeMM(nowDateMM).replaceAll("-","");
//        //广告位报告表
//        String tableReportAdName = TABLE_NAME_REPORT_AD_DAY_PREFIX + DateUtils.dateTimeMM(nowDateMM).replaceAll("-","");
//        //判断是否已存在表
//        if(reportGameDayMapper.existReportGameDayTable(tableReportGameName) <= 0) {
//            reportGameDayMapper.createReportGameDayTable(tableReportGameName);
//        }
//        if(reportAdMediaDayMapper.existReportAdMediaDayTable(tableReportAdName) <= 0) {
//            reportAdMediaDayMapper.createReportAdMediaDayTable(tableReportAdName);
//        }
//
//        //广告位类型字典
//        List<SysDictData> gameAdTypeList = getAdType(GAME_PLATFORM_AD_TYPE);
//
//
//        //多账号拉取数据
//        for (String cookies : cookiesList) {
//            if (!StringUtils.isEmpty(cookies)) {
//                // 循环获取数据（这里直接填100000，数据肯定没这么多页，等获取到空了就结束循环）
//                if (!gamesSimpleList.isEmpty()) {
//                    for (int n = 0; n < gamesSimpleList.size(); n++) {
//                        GamesSimpleVo game = gamesSimpleList.get(n);
//                        String gameId = game.getGameId(); //游戏ID
//                        String gameName = game.getGameName(); //游戏名称
//                        //下面两个日期都是昨天的-查询跨度
//                        long startDate = DateHandleUtil.getMinDataTimeStamp(3); //开始日期
//                        long endDate = DateHandleUtil.getMinDataTimeStamp(1); //结束日期
//                        LogUtils.info("获取Xiaomi应用报告数据-当前游戏:", gameName + " | " + gameId);
//                        LogUtils.info("获取Xiaomi应用报告数据-当前循环:", n);
//
//                        // 创建请求头并添加 cookies
//                        HttpHeaders headers = new HttpHeaders();
//                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                        headers.set("Origin", "https://dev.mi.com");
//                        headers.add(HttpHeaders.COOKIE, cookies);
//
//                        MultiValueMap<String, String> entity = new LinkedMultiValueMap<>();
//                        entity.add("publisherId", gameId);
//                        entity.add("startTime", String.valueOf(startDate));
//                        entity.add("endTime", String.valueOf(endDate));
//                        entity.add("page", "1");
//                        entity.add("pageSize", "10");
//                        entity.add("orderBy", "day");
//                        entity.add("desc", "desc");
//                        entity.add("styleId", "");
//                        entity.add("placementId", "");
//                        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(entity, headers);
//
//                        ResponseEntity<String> response = restTemplate.exchange(REPORT_GAME_DATA_URL, HttpMethod.POST, httpEntity, String.class);
//
//                        // 处理响应数据
//                        if (response.getStatusCode() == HttpStatus.OK) {
//                            // 解析响应数据
//                            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                            if (jsonObject == null) {
//                                LogUtils.info("获取Xiaomi应用报告数据-数据:", "数据返回异常 | " + response.getBody());
//                                // 结束循环
//                                continue;
//                            }
//                            if (jsonObject.getJSONObject("data") == null) {
//                                LogUtils.info("获取Xiaomi应用报告数据-数据:", "数据返回异常 | " + jsonObject);
//                                // 结束循环
//                                continue;
//                            }
//
//                            // 获取数据列表
//                            JSONArray dataList = jsonObject.getJSONObject("data").getJSONArray("dataList");
//                            String recordCount = jsonObject.getJSONObject("data").getString("total");
//                            //LogUtils.info("获取Xiaomi应用报告数据-数据:", dataList);
//                            LogUtils.info("获取Xiaomi应用报告数据-总条数:", recordCount);
//
//                            // 判断集合是否为空
//                            if (dataList == null || dataList.isEmpty()) {
//                                LogUtils.info("定时查询Xiaomi应用报告数据", "无更多数据");
//                                // 结束循环（结束的是最外层100000次的这个循环）
//                                continue;
//                            }
//                            // 循环处理数据集合
//                            for (int i = 0; i < dataList.size(); i++) {
//                                // 取出数据转成JSONObject对象
//                                JSONObject data = dataList.getJSONObject(i);
//                                // 打印数据或者进行其他处理
//                                //System.out.println(data);
//
//                                //应用报告
//                                //date:"20240929"
//                                String dayString = data.getString("day").replaceAll("/","");
//                                Date date = DateUtils.parseDate(data.getString("day")); //日期 2024/11/18
//                                //匹配已有广告日数据应用报告表
//                                ReportGameDay report = new ReportGameDay();
//                                report.setGameId(game.getGameId()); //渠道游戏ID
//                                report.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                report.setReportDate(date);//日期
//                                //设置表名
//                                tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + dayString.substring(0, dayString.length() - 2);
//
//                                report.setTableName(tableReportGameName);
//                                boolean isExist = false;
//                                ReportGameDay reportGameDay = reportGameDayMapper.selectReportGameDayByGame(report);
//                                //数据查重
//                                if (reportGameDay != null) {
//                                    LogUtils.info(StringUtils.format("定时查询Xiaomi广告位-应用报告数据-已存在报告数据 | {} | {} | {}",
//                                            reportGameDay.getGameName(), reportGameDay.getGameId(), date));
//                                    isExist = true;
//                                } else {
//                                    reportGameDay = new ReportGameDay();
//                                }
//                                //获取的日期是20240706这种格式的，这里调用方法处理成：2024-07-06，在入库
//                                reportGameDay.setReportDate(date); //日期
//                                reportGameDay.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                reportGameDay.setGameId(game.getGameId()); //渠道游戏ID
//                                reportGameDay.setGameName(game.getGameName()); //应用名称
//                                reportGameDay.setGameUuid(game.getGameId()); //应用UUID
//
//                                reportGameDay.setReportAppView(data.getDouble("adview")); //展现量-曝光量
//                                reportGameDay.setReportAppClick(data.getDouble("click")); //点击量
//                                reportGameDay.setReportAppClickRatio(data.getDouble("ctr")); //点击率
//                                reportGameDay.setReportAppEcpm(data.getDouble("ecpm")); //ecpm
//                                //reportGameDay.setReportAppCpc(data.getDouble("cpc")); //cpc
//                                reportGameDay.setReportAppIncome(data.getDouble("gain")); //收益
//                                reportGameDay.setTableName(tableReportGameName);
//
//                                if(isExist){
//                                    // 执行更新
//                                    reportGameDayMapper.updateReportGameDay(reportGameDay);
//                                } else {
//                                    // 执行插入
//                                    reportGameDayMapper.insertReportGameDay(reportGameDay);
//                                }
//                            }
//                        } else {
//                            LogUtils.error("定时查询Xiaomi应用报告数据", "请求失败：" + response.getStatusCode());
//                        }
//                        /* 获取应用报告中收益数据 */
//                        /*LogUtils.info("获取Xiaomi应用报告收益数据-当前游戏:", gameName + " | " + gameId);
//                        LogUtils.info("获取Xiaomi应用报告收益数据-当前循环:", n);
//
//                        entity = new LinkedMultiValueMap<>();
//                        entity.add("publisherId", gameId);
//                        entity.add("startTime", String.valueOf(startDate));
//                        entity.add("endTime", String.valueOf(endDate));
//                        entity.add("indicatorType", "gain");
//                        entity.add("metric", "");
//                        entity.add("styleId", "");
//                        entity.add("placementId", "");
//                        httpEntity = new HttpEntity<>(entity, headers);
//
//                        response = restTemplate.exchange(REPORT_GAME_TREND_DATA_URL, HttpMethod.POST, httpEntity, String.class);
//
//                        // 处理响应数据
//                        if (response.getStatusCode() == HttpStatus.OK) {
//                            // 解析响应数据
//                            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                            if (jsonObject == null) {
//                                LogUtils.info("获取Xiaomi应用报告收益数据-数据:", "数据返回异常 | " + response.getBody());
//                                // 结束循环
//                                continue;
//                            }
//                            if (jsonObject.getJSONObject("data") == null) {
//                                LogUtils.info("获取Xiaomi应用报告收益数据-数据:", "数据返回异常 | " + jsonObject);
//                                // 结束循环
//                                continue;
//                            }
//
//                            // 获取数据列表
//                            JSONArray dataList = jsonObject.getJSONObject("data").getJSONArray("trendVoList");
//                            LogUtils.info("获取Xiaomi应用报告收益数据-数据:", dataList);
//
//                            // 判断集合是否为空
//                            if (dataList == null || dataList.isEmpty()) {
//                                LogUtils.info("定时查询Xiaomi应用报告数据", "无更多数据");
//                                // 结束循环（结束的是最外层100000次的这个循环）
//                                continue;
//                            }
//                            JSONObject incomeData = dataList.getJSONObject(0);
//                            JSONArray incomeDataList = incomeData.getJSONArray("statisticList");
//                            if (incomeDataList == null || incomeDataList.isEmpty()) {
//                                LogUtils.info("定时查询Xiaomi应用报告数据", "无更多数据");
//                                // 结束循环（结束的是最外层100000次的这个循环）
//                                continue;
//                            }
//
//                            // 循环处理数据集合
//                            for (int i = 0; i < incomeDataList.size(); i++) {
//                                // 取出数据转成JSONObject对象
//                                JSONObject data = incomeDataList.getJSONObject(i);
//                                // 打印数据或者进行其他处理
//                                //System.out.println(data);
//
//                                //应用报告
//                                //date:"20240929"
//                                String dayString = data.getString("day");//2024-11-13
//                                Date date = data.getDate("day"); //日期 2024-11-13
//                                //匹配已有广告日数据应用报告表
//                                ReportGameDay report = new ReportGameDay();
//                                report.setGameId(game.getGameId()); //渠道游戏ID
//                                report.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                report.setReportDate(date);//日期
//                                //设置表名
//                                tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX +
//                                        dayString.replaceAll("-","").substring(0, dayString.length() - 2);
//
//                                report.setTableName(tableReportGameName);
//                                ReportGameDay reportGameDay = reportGameDayMapper.selectReportGameDayByGame(report);
//                                //数据查重
//                                if (reportGameDay == null) {
//                                    LogUtils.error("定时查询Xiaomi应用报告数据-查询报告数据异常", report.toString());
//                                    continue;
//                                }
//                                reportGameDay.setReportAppIncome(data.getDouble("value"));
//                                reportGameDay.setTableName(tableReportGameName);
//                                // 执行更新
//                                reportGameDayMapper.updateReportGameDay(reportGameDay);
//                            }
//                        } else {
//                            LogUtils.error("定时查询Xiaomi应用报告数据", "请求失败：" + response.getStatusCode());
//                        }*/
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取应用广告位报告数据
//     * https://dev.mi.com/union/static/index.html?userId=2895450891#/analytics
//     */
//    public void getReportGameAdData() {
//        // 获取cookies
//        List<String> cookiesList = getCookies();
//        if (cookiesList == null || cookiesList.isEmpty()) {
//            LogUtils.info("获取 Cookies 失败");
//            return;
//        }
//
//        //1.先去库里把所有游戏查询出来
//        Games games = new Games();
//        //games.setGameId(611192L);
//        games.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);
//        List<Games> gamesList = gamesMapper.selectGamesList(games);
//        List<GamesSimpleVo> gamesSimpleList = new ArrayList<>();
//        for (Games game : gamesList) {
//            GamesSimpleVo simpleVo = new GamesSimpleVo(game.getGameId(),game.getPackageName(),
//                    game.getGameName(), game.getVersionName(), game.getVersionCode());
//            gamesSimpleList.add(simpleVo);
//        }
//
//        //创建报告相关表
//        //匹配当前时间对应的表名, report_vivo_game_day_202410, 根据月份拆分表
//        Date nowDate = DateUtils.parseDate(DateUtils.getDate());
//        //Date nowDateMM = DateUtils.parseDate(DateUtils.getDateMM());
//        //匹配昨天时间对应的表名
//        Date nowDateMM = DateUtils.parseDate(DateHandleUtil.getDataTimeYYMM(1));
//
//        String tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + DateUtils.dateTimeMM(nowDateMM).replaceAll("-","");
//        //广告位报告表
//        String tableReportAdName = TABLE_NAME_REPORT_AD_DAY_PREFIX + DateUtils.dateTimeMM(nowDateMM).replaceAll("-","");
//        //判断是否已存在表
//        if(reportGameDayMapper.existReportGameDayTable(tableReportGameName) <= 0) {
//            reportGameDayMapper.createReportGameDayTable(tableReportGameName);
//        }
//        if(reportAdMediaDayMapper.existReportAdMediaDayTable(tableReportAdName) <= 0) {
//            reportAdMediaDayMapper.createReportAdMediaDayTable(tableReportAdName);
//        }
//        //广告位类型字典
//        List<SysDictData> gameAdTypeList = getAdType(GAME_PLATFORM_AD_TYPE);
//
//        //多账号拉取数据
//        for (String cookies : cookiesList) {
//            if (!StringUtils.isEmpty(cookies)) {
//                // 循环获取数据（这里直接填100000，数据肯定没这么多页，等获取到空了就结束循环）
//                if (!gamesSimpleList.isEmpty()) {
//                    for (int n = 0; n < gamesSimpleList.size(); n++) {
//                        GamesSimpleVo game = gamesSimpleList.get(n);
//                        String gameId = game.getGameId(); //游戏ID
//                        String gameName = game.getGameName(); //游戏名称
//                        //下面两个日期都是昨天的-查询跨度
//                        long startDate = DateHandleUtil.getMinDataTimeStamp(3); //开始日期
//                        long endDate = DateHandleUtil.getMinDataTimeStamp(1); //结束日期
//                        LogUtils.info("获取Xiaomi广告位报告数据-当前游戏:", gameName + " | " + gameId);
//                        LogUtils.info("获取Xiaomi广告位报告数据-当前循环:", n);
//
//                        // 创建请求头并添加 cookies
//                        HttpHeaders headers = new HttpHeaders();
//                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                        headers.set("Origin", "https://dev.mi.com");
//                        headers.add(HttpHeaders.COOKIE, cookies);
//
//                        MultiValueMap<String, String> entity = new LinkedMultiValueMap<>();
//                        entity.add("publisherId", gameId);
//                        entity.add("startTime", String.valueOf(startDate));
//                        entity.add("endTime", String.valueOf(endDate));
//                        entity.add("page", "1");
//                        entity.add("pageSize", "200");
//                        entity.add("orderBy", "day");
//                        entity.add("desc", "desc");
//                        entity.add("styleId", "");
//                        entity.add("placementId", "selectAll");
//                        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(entity, headers);
//
//                        ResponseEntity<String> response = restTemplate.exchange(REPORT_GAME_DATA_URL, HttpMethod.POST, httpEntity, String.class);
//
//                        // 处理响应数据
//                        if (response.getStatusCode() == HttpStatus.OK) {
//                            // 解析响应数据
//                            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                            if (jsonObject == null) {
//                                LogUtils.info("获取Xiaomi广告位报告数据-数据:", "数据返回异常 | " + response.getBody());
//                                // 结束循环
//                                continue;
//                            }
//                            if (jsonObject.getJSONObject("data") == null) {
//                                LogUtils.info("获取Xiaomi广告位报告数据-数据:", "数据返回异常 | " + jsonObject);
//                                // 结束循环
//                                continue;
//                            }
//
//                            // 获取数据列表
//                            JSONArray dataList = jsonObject.getJSONObject("data").getJSONArray("dataList");
//                            String recordCount = jsonObject.getJSONObject("data").getString("total");
//                            //LogUtils.info("获取Xiaomi广告位报告数据-数据:", dataList);
//                            LogUtils.info("获取Xiaomi广告位报告数据-总条数:", recordCount);
//
//                            // 判断集合是否为空
//                            if (dataList == null || dataList.isEmpty()) {
//                                LogUtils.info("定时查询Xiaomi广告位报告数据", "无更多数据");
//                                // 结束循环（结束的是最外层100000次的这个循环）
//                                continue;
//                            }
//                            // 循环处理数据集合
//                            for (int i = 0; i < dataList.size(); i++) {
//                                // 取出数据转成JSONObject对象
//                                JSONObject data = dataList.getJSONObject(i);
//                                // 打印数据或者进行其他处理
//                                //System.out.println(data);
//
//                                //应用报告
//                                //date:"20240929"
//                                String dayString = data.getString("day").replaceAll("/","");
//                                String placementId = data.getString("placementId"); // 广告位ID
//                                String adPlaceName = data.getString("placementName"); //广告位名称
//                                Date date = DateUtils.parseDate(data.getString("day")); //日期 2024/11/18
//                                //匹配已有广告位报告数据报告表
//                                ReportAdMediaDay report = new ReportAdMediaDay();
//                                report.setGameId(game.getGameId()); //渠道游戏ID
//                                report.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                report.setAdUuid(placementId); //广告位UUID
//                                report.setReportDate(date);//日期
//                                //设置表名
//                                tableReportAdName = TABLE_NAME_REPORT_AD_DAY_PREFIX + dayString.substring(0, dayString.length() - 2);
//
//                                report.setTableName(tableReportAdName);
//                                boolean isExist = false;
//                                ReportAdMediaDay reportAdMediaDay = reportAdMediaDayMapper.selectReportAdMediaDayByGame(report);
//                                //数据查重
//                                if (reportAdMediaDay != null) {
//                                    LogUtils.info(StringUtils.format("定时查询Xiaomi广告位报告数据-应用报告数据-已存在报告数据 | {} | {} | {}",
//                                            reportAdMediaDay.getGameName(), reportAdMediaDay.getGameId(), date));
//                                    isExist = true;
//                                } else {
//                                    reportAdMediaDay = new ReportAdMediaDay();
//                                }
//                                // 1.封装数据
//                                //获取的日期是20240706这种格式的，这里调用方法处理成：2024-07-06，在入库
//                                reportAdMediaDay.setReportDate(date); //日期
//                                //reportAdMediaDay.setPositionId(data.getLong("positionId")); //广告位ID
//                                reportAdMediaDay.setGameId(game.getGameId()); //渠道游戏ID
//                                reportAdMediaDay.setGameName(game.getGameName()); //媒体名称-游戏名称
//                                reportAdMediaDay.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//
//                                reportAdMediaDay.setAdUuid(placementId); //广告位UUID
//                                //oppo渠道 1：Banner横幅 2：插屏 4：开屏 8：原生 64：激励视频
//                                //int adPlaceTypeOriginal = data.getInteger("showTypeId"); //广告位类型(渠道原始值)
//                                //reportAdMediaDay.setReportAdPlaceTypeOriginal(adPlaceTypeOriginal);
//                                reportAdMediaDay.setReportAdPlaceName(adPlaceName); //广告位名称
//                                //无样式广告位类型 系统开屏/横幅/插屏
//                                //有样式广告位类型 原生横幅-上图下文/插屏全屏-左图右文
//                                String adPlaceTypeName = adPlaceName.split("-")[0];//以-分割，第一位为广告位类型
//                                adPlaceTypeName = adPlaceTypeName.replace(" ","");
//                                // 匹配标签  系统开屏:0 横幅:1 半插:2 原生模板:3 激励视频:4 失败:5 原生模板横幅:6 全插:7 原生自渲染:8 原生自渲染横幅:9
//                                // vivo后台广告类型  系统开屏:2  横幅:3  插屏:4  原生:5  激励视频:9
//                                if(adPlaceTypeName.contains("半")){
//                                    reportAdMediaDay.setReportAdPlaceType(2); // 半插:2
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(4);
//                                } else if(adPlaceTypeName.contains("全")){
//                                    reportAdMediaDay.setReportAdPlaceType(7); // 全插:7
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(4);
//                                } else if(adPlaceTypeName.contains("原生模板横幅") || adPlaceTypeName.contains("原生横幅")){
//                                    reportAdMediaDay.setReportAdPlaceType(6); // 原生模板横幅:6
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(5);
//                                } else if(adPlaceTypeName.contains("系统开屏")){
//                                    reportAdMediaDay.setReportAdPlaceType(0); // 系统开屏:0
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(2);
//                                } else if(adPlaceTypeName.contains("激励视频")){
//                                    reportAdMediaDay.setReportAdPlaceType(4); //激励视频:4
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(9);
//                                } else if(adPlaceTypeName.equals("横幅")){
//                                    reportAdMediaDay.setReportAdPlaceType(1); // 横幅:1
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(3);
//                                } else if(adPlaceTypeName.equals("原生") || adPlaceTypeName.equals("原生模板")){
//                                    reportAdMediaDay.setReportAdPlaceType(3); // 原生模板:3
//                                    reportAdMediaDay.setReportAdPlaceTypeOriginal(5);
//                                } else {
//                                    for (SysDictData sysDictData : gameAdTypeList) {
//                                        String dictLabel = sysDictData.getDictLabel();
//                                        // 匹配标签  系统开屏:0 横幅:1 插屏:2 原生插屏:3
//                                        if(adPlaceTypeName.equals(dictLabel)){
//                                            reportAdMediaDay.setReportAdPlaceType(Integer.valueOf(sysDictData.getDictValue())); // 设置广告类型
//                                        }
//                                    }
//                                }
//
//                                reportAdMediaDay.setReportAppView(data.getDouble("adview")); //曝光量
//                                reportAdMediaDay.setReportAppClick(data.getDouble("click")); //点击量
//                                reportAdMediaDay.setReportAppClickRatio(data.getDouble("ctr")); //点击率
//                                reportAdMediaDay.setReportAppEcpm(data.getDouble("ecpm")); //ecpm, 千次展现收入
//                                //reportAdMediaDay.setReportAppCpc(data.getDouble("cpc")); //cpc
//                                reportAdMediaDay.setReportAppIncome(data.getDouble("gain")); //收入，单位元，保留四位小数
//
//                                //匹配当前时间对应的活动日志表名
//                                String tableName = TABLE_NAME_PREFIX + dayString;
//                                //判断是否已存在该日期活动日志表
//                                if(userActiveLogMapper.existUserLogTable(tableName) > 0) {
//                                    //查询该广告位ID在活动日志表中的记录总数
//                                    UserActiveLog userActiveLog = new UserActiveLog();
//                                    userActiveLog.setGameId(game.getGameId());
//                                    userActiveLog.setOnlinePlatformType(String.valueOf(reportAdMediaDay.getOnlinePlatformType()));
//                                    userActiveLog.setAdPlaceId(reportAdMediaDay.getAdUuid());
//                                    //userActiveLog.setAdPlaceType(reportAdMediaDay.getReportAdPlaceType());
//                                    userActiveLog.setStartDate(reportAdMediaDay.getReportDate());
//                                    userActiveLog.setTableName(tableName);
//                                    //Long num = userActiveLogMapper.selectUserActiveLogByAd(userActiveLog);
//                                    //if(num!=null && num>0){
//                                    //    reportAdMediaDay.setRequestNum((double)num); //服务器请求数
//                                    //}
//                                    //查询游戏是否有活动日志
//                                    List<UserActiveLog> userActiveLogList = userActiveLogMapper.selectUserActiveLogList(userActiveLog);
//                                    if (userActiveLogList != null && !userActiveLogList.isEmpty()) {
//                                        AtomicReference<Double> showNum = new AtomicReference<>((double) 0);//展示数
//                                        AtomicReference<Double> clickNum = new AtomicReference<>((double) 0);//点击数
//                                        AtomicReference<Double> requestNum = new AtomicReference<>((double) 0);//请求数
//                                        // 以游戏广告位动作进行分组
//                                        // 加载-0 显示-1 错误-2 关闭-3 点击-4 请求-5
//                                        Map<Integer, List<UserActiveLog>> actionLogs =
//                                                userActiveLogList.stream().collect(Collectors.groupingBy(UserActiveLog::getAdAction));
//                                        actionLogs.forEach((adAction, adActionLogs) -> {
//                                            LogUtils.info("定时查询Xiaomi广告位报告列表-广告位报告数据-广告位动作:", adAction);
//                                            //该广告位动作统计总数
//                                            long totalNum = adActionLogs.size();
//                                            if (adAction == 0) {
//                                                //加载数
//                                            } else if (adAction == 1) {
//                                                //显示数
//                                                showNum.set((double) totalNum);
//                                            } else if (adAction == 2) {
//                                                //错误数
//                                            } else if (adAction == 3) {
//                                                //关闭数
//                                            } else if (adAction == 4) {
//                                                //点击数
//                                                clickNum.set((double) totalNum);
//                                            } else if (adAction == 5) {
//                                                //请求数
//                                                requestNum.set((double) totalNum);
//                                            }
//                                        });
//                                        if(requestNum.get()!=null && requestNum.get()>0){
//                                            reportAdMediaDay.setRequestNum(requestNum.get()); //广告位动作为请求类型的累加值
//                                        }
//                                        if(showNum.get()!=null && showNum.get()>0 && clickNum.get()!=null && clickNum.get()>0){
//                                            Double ratioValue = NumberUtil.div(clickNum.get(), showNum.get(), 4);
//                                            reportAdMediaDay.setReportWrongClickRatio(ratioValue); //误触率(点击量/展现量),来源用户活动日志
//                                        }
//                                        if(showNum.get()!=null && showNum.get()>0 && requestNum.get()!=null && requestNum.get()>0){
//                                            Double ratioValue = NumberUtil.div(showNum.get(), requestNum.get(), 4);
//                                            reportAdMediaDay.setReportFillingRatio(ratioValue); //填充率(展现量/请求数),来源用户活动日志
//                                        }
//                                    } else {
//                                        LogUtils.info("定时查询Xiaomi广告位报告列表-广告位报告数据-该游戏没有活动日志:", userActiveLog.toString());
//                                    }
//                                }
//
//                                reportAdMediaDay.setTableName(tableReportAdName);
//                                if(isExist){
//                                    // 执行插入
//                                    reportAdMediaDayMapper.updateReportAdMediaDay(reportAdMediaDay);
//                                } else {
//                                    // 执行插入
//                                    reportAdMediaDayMapper.insertReportAdMediaDay(reportAdMediaDay);
//                                }
//                            }
//                        } else {
//                            LogUtils.error("定时查询Xiaomi广告位报告数据", "请求失败：" + response.getStatusCode());
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取应用报告详细概览
//     * https://dev.mi.com/union/static/index.html?userId=2895450891#/analytics
//     */
//    public void getReportGameDetailData() {
//        // 获取cookies
//        List<String> cookiesList = getCookies();
//        if (cookiesList == null || cookiesList.isEmpty()) {
//            LogUtils.info("获取 Cookies 失败");
//            return;
//        }
//
//        //1.先去库里把所有游戏查询出来
//        Games games = new Games();
//        games.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE);
//        List<Games> gamesList = gamesMapper.selectGamesList(games);
//        List<GamesSimpleVo> gamesSimpleList = new ArrayList<>();
//        for (Games game : gamesList) {
//            GamesSimpleVo simpleVo = new GamesSimpleVo(game.getGameId(),game.getPackageName(),
//                    game.getGameName(), game.getVersionName(), game.getVersionCode());
//            gamesSimpleList.add(simpleVo);
//        }
//
//        Date nowDate = DateUtils.parseDate(DateUtils.getDate());
//        //Date nowDateMM = DateUtils.parseDate(DateUtils.getDateMM());
//        //匹配昨天时间对应的表名
//        Date nowDateMM = DateUtils.parseDate(DateHandleUtil.getDataTimeYYMM(1));
//
//        //广告日报告报告表
//        String tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + DateUtils.dateTimeMM(nowDateMM).replaceAll("-","");
//        //判断是否已存在表
//        if(reportGameDayMapper.existReportGameDayTable(tableReportGameName) <= 0) {
//            LogUtils.error("获取Xiaomi广告日报告数据-获取广告日报告表异常:", tableReportGameName);
//        }
//
//        //多账号拉取数据
//        for (String cookies : cookiesList) {
//            if (!StringUtils.isEmpty(cookies)) {
//                if (!gamesSimpleList.isEmpty()) {
//                    for (int n = 0; n < gamesSimpleList.size(); n++) {
//                        GamesSimpleVo game = gamesSimpleList.get(n);
//                        String gameId = game.getGameId(); //游戏ID
//                        String gameName = game.getGameName(); //游戏名称
//                        String packageName = game.getPackageName(); //游戏名称
//                        //下面两个日期都是昨天的-查询跨度
//                        String startDate = DateHandleUtil.getDataTime(3); //开始日期
//                        String endDate = DateHandleUtil.getDataTime(1); //结束日期
//                        LogUtils.info("获取Xiaomi数据概览分析报告-当前游戏:", gameName + " | " + gameId);
//                        LogUtils.info("获取Xiaomi数据概览分析报告-当前循环:", n);
//
//                        // 请求参数
//                        String fieldNames = "viewDownRate,newActiveFuidUv,activeFuidUv,payAmt,payUv,actArpu";
//
//                        String url = String.format("https://dev.mi.com/gameCenter/web/prod-api/statistical-web/api/report/gamedata/v2?" +
//                                        "packageName=%s&gameType=1&fieldNames=%s&startDt=%s&endDt=%s&channel=all&appId=%s&appName=%s" +
//                                        "&userId=2895450891&devId=1123623&namespaceValue=0&devAppId=2882303761520358357",
//                                packageName, fieldNames, startDate, endDate, gameId, gameName);
//
//                        // 创建请求头并添加 cookies
//                        HttpHeaders headers = new HttpHeaders();
//                        headers.setContentType(MediaType.APPLICATION_JSON);
//                        headers.add(HttpHeaders.COOKIE, cookies);
//
//                        // 创建请求实体
//                        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//                        ResponseEntity<String> response;
//                        try {
//                            // 发送 GET 请求
//                            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//                        } catch (RuntimeException e){
//                            LogUtils.error("获取Xiaomi数据概览分析报告接口报错'请求地址：{}.", url, e);
//                            continue;
//                        }
//
//                        try {
//                            // 处理响应数据
//                            if (response.getStatusCode() == HttpStatus.OK) {
//                                // 解析响应数据
//                                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                                if (jsonObject == null) {
//                                    LogUtils.info("获取Xiaomi数据概览分析报告:", "数据返回异常 | " + response);
//                                    // 结束循环
//                                    continue;
//                                }
//                                if (jsonObject.getJSONArray("data") == null) {
//                                    LogUtils.info("获取Xiaomi数据概览分析报告:", "数据返回异常 | " +jsonObject);
//                                    // 结束循环
//                                    continue;
//                                }
//                                // 获取数据列表
//                                JSONArray dataList = jsonObject.getJSONArray("data");
//                                //LogUtils.info("获取Xiaomi数据概览分析报告-数据:", dataList);
//
//                                //判断集合是否为空
//                                if (dataList == null || dataList.isEmpty()) {
//                                    LogUtils.info("获取Xiaomi数据概览分析报告-无更多数据:", game.toString());
//                                    // 结束循环
//                                    continue;
//                                }
//
//                                for (int i = 0; i < dataList.size(); i++) {
//                                    // 取出数据转成JSONObject对象
//                                    JSONObject data = dataList.getJSONObject(i);
//                                    // 打印数据或者进行其他处理
//                                    //LogUtils.info("获取Xiaomi数据概览分析报告:", data);
//
//                                    String dayString = data.getString("dt");
//                                    Date date = DateUtils.parseDate(dayString); //日期 2024-11-14
//                                    //匹配已有广告日数据报告表
//                                    ReportGameDay report = new ReportGameDay();
//                                    report.setGameId(game.getGameId()); //渠道游戏ID
//                                    report.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                    report.setReportDate(date);//日期
//                                    //设置表名
//                                    String dataString = dayString.replaceAll("-","");
//                                    tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + dataString.substring(0, dataString.length() - 2);
//
//                                    report.setTableName(tableReportGameName);
//                                    ReportGameDay reportGameDay = reportGameDayMapper.selectReportGameDayByGame(report);
//
//                                    if (reportGameDay == null) {
//                                        LogUtils.info(StringUtils.format("获取Xiaomi数据概览分析报告-匹配日报告数据异常 | {} | {} | {}",
//                                                game.getGameName(), game.getGameId(), date));
//                                        continue;
//                                    }
//                                    reportGameDay.setTableName(tableReportGameName);
//
//                                    //新增用户明细
//                                    reportGameDay.setUserNewUserUv(data.getDouble("newActiveFuidUv")); // 新增用户
//                                    //reportGameDay.setUserNewUserAvgDuration(data.getDouble("avg_duration")); // 新增用户人均使用时长(分钟)
//
//                                    if(reportGameDay.getReportAppIncome()!=null && reportGameDay.getReportAppIncome()>0
//                                            && reportGameDay.getUserNewUserUv()!=null && reportGameDay.getUserNewUserUv()>0){
//                                        Double reportNewUserArpu = NumberUtil.div(reportGameDay.getReportAppIncome(), reportGameDay.getUserNewUserUv(), 4);
//                                        reportGameDay.setReportNewUserArpu(reportNewUserArpu); //新增用户ARPU(预估收益/新增用户数)
//                                    }
//
//                                    //活跃用户明细
//                                    reportGameDay.setUserActiveUserUv(data.getDouble("activeFuidUv")); // 日活跃用户数
//                                    if(reportGameDay.getReportAppView()!=null && reportGameDay.getReportAppView()>0
//                                            && reportGameDay.getUserActiveUserUv()!=null && reportGameDay.getUserActiveUserUv()>0){
//                                        Double reportAverageView = NumberUtil.div(reportGameDay.getReportAppView(), reportGameDay.getUserActiveUserUv(), 4);
//                                        reportGameDay.setReportAverageView(reportAverageView); //总均展(展现量/活跃用户数)
//
//                                        //更新广告位数据报告活跃用户数据
//                                        updateAdData(GAME_ONLINE_PLATFORM_TYPE, game, date, dataString, reportGameDay);
//                                    }
//                                    if(reportGameDay.getReportAppIncome()!=null && reportGameDay.getReportAppIncome()>0
//                                            && reportGameDay.getUserActiveUserUv()!=null && reportGameDay.getUserActiveUserUv()>0){
//                                        Double reportActiveUserArpu = NumberUtil.div(reportGameDay.getReportAppIncome(), reportGameDay.getUserActiveUserUv(), 4);
//                                        reportGameDay.setReportActiveUserArpu(reportActiveUserArpu); //活跃用户ARPU(预估收益/活跃用户数)
//                                    }
//                                    //更新广告日报告数据
//                                    reportGameDayMapper.updateReportGameDay(reportGameDay);
//                                    //LogUtils.info("获取Xiaomi广告日报告数据-活跃用户明细报告更新:", reportGameDay.toString());
//                                }
//                            } else {
//                                LogUtils.error("获取Xiaomi数据概览分析报告-请求失败，状态码：", response.getStatusCode());
//                            }
//                        } catch (RuntimeException e) {
//                            LogUtils.error("获取Xiaomi用户行为分析数据报告-获取数据概览分析报告异常:{}'.", url + " | " +response, e);
//                        }
//
//                        try {
//                            // 暂停2秒
//                            TimeUnit.MILLISECONDS.sleep(800);
//
//                            /*获取计算 新增用户人均使用时长(分钟)*/
//                            // 请求参数
//                            url = String.format("https://dev.mi.com/gameCenter/web/prod-api/statistical-web/api/behavior/listUserBehavior?" +
//                                            "packageName=%s&startDt=%s&endDt=%s&categoryType=duration" +
//                                            "&appId=%s&appName=%s&userId=2895450891&devId=1123623&namespaceValue=0&devAppId=2882303761520358357",
//                                    packageName, startDate, endDate, gameId, gameName);
//                            try {
//                                // 发送 GET 请求
//                                response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//                            } catch (RuntimeException e){
//                                LogUtils.error("获取Xiaomi用户行为分析数据报告接口报错'请求地址：{}.", url, e);
//                                continue;
//                            }
//
//                            // 处理响应数据
//                            if (response.getStatusCode() == HttpStatus.OK) {
//                                // 解析响应数据
//                                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                                if (jsonObject == null) {
//                                    LogUtils.info("获取Xiaomi用户行为分析数据报告:", "数据返回异常 | " + response);
//                                    // 结束循环
//                                    continue;
//                                }
//                                if (jsonObject.getJSONArray("data") == null) {
//                                    LogUtils.info("获取Xiaomi用户行为分析数据报告:", "数据返回异常 | " +jsonObject);
//                                    // 结束循环
//                                    continue;
//                                }
//                                // 获取数据列表
//                                JSONArray dataList = jsonObject.getJSONArray("data");
//                                //LogUtils.info("获取Xiaomi用户行为分析数据报告-数据:", dataList);
//
//                                //判断集合是否为空
//                                if (dataList == null || dataList.isEmpty()) {
//                                    LogUtils.info("获取Xiaomi用户行为分析数据报告-无更多数据:", game.toString());
//                                    // 结束循环
//                                    continue;
//                                }
//
//                                for (int i = 0; i < dataList.size(); i++) {
//                                    // 取出数据转成JSONObject对象
//                                    JSONObject data = dataList.getJSONObject(i);
//                                    // 打印数据或者进行其他处理
//                                    //LogUtils.info("获取Xiaomi用户行为分析数据报告:", data);
//
//                                    String dayString = data.getString("dt");
//                                    Date date = DateUtils.parseDate(dayString); //日期 2024-11-14
//                                    //匹配已有广告日数据报告表
//                                    ReportGameDay report = new ReportGameDay();
//                                    report.setGameId(game.getGameId()); //渠道游戏ID
//                                    report.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                    report.setReportDate(date);//日期
//                                    //设置表名
//                                    String dataString = dayString.replaceAll("-","");
//                                    tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + dataString.substring(0, dataString.length() - 2);
//
//                                    report.setTableName(tableReportGameName);
//                                    ReportGameDay reportGameDay = reportGameDayMapper.selectReportGameDayByGame(report);
//
//                                    if (reportGameDay == null) {
//                                        LogUtils.info(StringUtils.format("获取Xiaomi用户行为分析数据报告-匹配日报告数据异常 | {} | {} | {}",
//                                                game.getGameName(), game.getGameId(), date));
//                                        continue;
//                                    }
//                                    reportGameDay.setTableName(tableReportGameName);
//
//                                    //计算用户使用时长情况
//                                    int usersSum1 = data.getIntValue("usersSum1", 0);
//                                    int usersSum2 = data.getIntValue("usersSum2", 0);
//                                    int usersSum3 = data.getIntValue("usersSum3", 0);
//                                    int usersSum4 = data.getIntValue("usersSum4", 0);
//                                    int usersSum5 = data.getIntValue("usersSum5", 0);
//                                    int usersSum6 = data.getIntValue("usersSum6", 0);
//                                    int usersSum7 = data.getIntValue("usersSum7", 0);
//                                    int addSum = NumberUtil.add(usersSum1, usersSum2, usersSum3,
//                                            usersSum4, usersSum5, usersSum6, usersSum7).intValue();
//                                    if(addSum>0){
//                                        double usersSum1Dob = NumberUtil.mul(usersSum1, 0.5);
//                                        double usersSum2Dob = NumberUtil.mul(usersSum2, 3);
//                                        double usersSum3Dob = NumberUtil.mul(usersSum3, 7.5);
//                                        double usersSum4Dob = NumberUtil.mul(usersSum4, 15);
//                                        double usersSum5Dob = NumberUtil.mul(usersSum5, 40);
//                                        double usersSum6Dob = NumberUtil.mul(usersSum6, 90);
//                                        double usersSum7Dob = NumberUtil.mul(usersSum7, 120);
//                                        double addSumDou = NumberUtil.add(usersSum1Dob, usersSum2Dob, usersSum3Dob,
//                                                usersSum4Dob, usersSum5Dob, usersSum6Dob, usersSum7Dob).doubleValue();
//                                        if(addSumDou>0){
//                                            double avgDuration = NumberUtil.div(addSumDou, addSum, 4);
//                                            reportGameDay.setUserNewUserAvgDuration(avgDuration); // 新增用户人均使用时长(分钟)
//                                            //更新广告日报告数据
//                                            reportGameDayMapper.updateReportGameDay(reportGameDay);
//                                        }
//                                    }
//                                }
//                            } else {
//                                LogUtils.error("获取Xiaomi用户行为分析数据报告-请求失败，状态码：", response.getStatusCode());
//                            }
//                        } catch (InterruptedException e) {
//                            LogUtils.error("获取Xiaomi用户行为分析数据报告:{}',暂停期间发生中断.", "获取用户行为分析报告接口", e);
//                            Thread.currentThread().interrupt(); // 重置中断状态
//                        } catch (RuntimeException e) {
//                            LogUtils.error("获取Xiaomi用户行为分析数据报告-获取用户行为分析数据报告异常:{}'.", url + " | " +response, e);
//                        }
//
//                        try {
//                            // 暂停2秒
//                            TimeUnit.MILLISECONDS.sleep(800);
//
//                            /*获取用户粘性数据*/
//                            // 请求参数
//                            fieldNames = "loginFuidUv,newRetainUv1,newRetainUv3,newRetainUv7,newRetainUv30,retainUv1,retainUv3,retainUv7,retainUv30,loseUv,loseRate";
//                            // 请求参数
//                            url = String.format("https://dev.mi.com/gameCenter/web/prod-api/statistical-web/api/report/gamedata/v2?" +
//                                            "packageName=%s&gameType=1&fieldNames=%s" +
//                                            "&startDt=%s&endDt=%s&channel=all&appId=%s&appName=%s" +
//                                            "&userId=2895450891&devId=1123623&namespaceValue=0&devAppId=2882303761520358357",
//                                    packageName, fieldNames, startDate, endDate, gameId, gameName);
//                            try {
//                                // 发送 GET 请求
//                                response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//                            } catch (RuntimeException e){
//                                LogUtils.error("获取Xiaomi获取用户粘性数据报告接口报错'请求地址：{}.", url, e);
//                                continue;
//                            }
//
//                            // 处理响应数据
//                            if (response.getStatusCode() == HttpStatus.OK) {
//                                // 解析响应数据
//                                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
//                                if (jsonObject == null) {
//                                    LogUtils.info("获取Xiaomi获取用户粘性数据报告:", "数据返回异常 | " + response);
//                                    // 结束循环
//                                    continue;
//                                }
//                                if (jsonObject.getJSONArray("data") == null) {
//                                    LogUtils.info("获取Xiaomi获取用户粘性数据报告:", "数据返回异常 | " +jsonObject);
//                                    // 结束循环
//                                    continue;
//                                }
//                                // 获取数据列表
//                                JSONArray dataList = jsonObject.getJSONArray("data");
//                                //LogUtils.info("获取Xiaomi获取用户粘性数据报告-数据:", dataList);
//
//                                //判断集合是否为空
//                                if (dataList == null || dataList.isEmpty()) {
//                                    LogUtils.info("获取Xiaomi获取用户粘性数据报告-无更多数据:", game.toString());
//                                    // 结束循环
//                                    continue;
//                                }
//
//                                for (int i = 0; i < dataList.size(); i++) {
//                                    // 取出数据转成JSONObject对象
//                                    JSONObject data = dataList.getJSONObject(i);
//                                    // 打印数据或者进行其他处理
//                                    //LogUtils.info("获取Xiaomi获取用户粘性数据报告:", data);
//
//                                    String dayString = data.getString("dt");
//                                    Date date = DateUtils.parseDate(dayString); //日期 2024-11-14
//                                    //匹配已有广告日数据报告表
//                                    ReportGameDay report = new ReportGameDay();
//                                    report.setGameId(game.getGameId()); //渠道游戏ID
//                                    report.setOnlinePlatformType(GAME_ONLINE_PLATFORM_TYPE); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//                                    report.setReportDate(date);//日期
//                                    //设置表名
//                                    String dataString = dayString.replaceAll("-","");
//                                    tableReportGameName = TABLE_NAME_REPORT_GAME_DAY_PREFIX + dataString.substring(0, dataString.length() - 2);
//
////                                    if(game.getGameId().equals("2882303761520343988")){
////                                        LogUtils.info("获取Xiaomi用户行为分析数据报告-", game + " | " + report);
////                                    }
//                                    report.setTableName(tableReportGameName);
//                                    ReportGameDay reportGameDay = reportGameDayMapper.selectReportGameDayByGame(report);
//
//                                    if (reportGameDay == null) {
//                                        LogUtils.info(StringUtils.format("获取Xiaomi用户行为分析数据报告-匹配日报告数据异常 | {} | {} | {}",
//                                                game.getGameName(), game.getGameId(), date));
//                                        continue;
//                                    }
//                                    reportGameDay.setTableName(tableReportGameName);
//
//                                    //新增用户留存分析明细
//                                    reportGameDay.setUserDay1NewUserRate(Double.valueOf(StringUtils.isEmpty(data.getString("newRetainUv1"))?
//                                            "0" : data.getString("newRetainUv1"))); // 次日留存率
//                                    reportGameDay.setUserDay3NewUserRate(Double.valueOf(StringUtils.isEmpty(data.getString("newRetainUv3"))?
//                                            "0" : data.getString("newRetainUv3"))); // 3日留存率
//                                    //更新广告日报告数据
//                                    reportGameDayMapper.updateReportGameDay(reportGameDay);
//                                }
//                            } else {
//                                LogUtils.error("获取Xiaomi获取用户粘性数据报告-请求失败，状态码：", response.getStatusCode());
//                            }
//                        } catch (InterruptedException e) {
//                            LogUtils.error("获取Xiaomi用户行为分析数据报告:{}',暂停期间发生中断.", "获取用户粘性数据报告接口", e);
//                            Thread.currentThread().interrupt(); // 重置中断状态
//                        } catch (RuntimeException e) {
//                            LogUtils.error("获取Xiaomi用户行为分析数据报告-获取用户粘性数据报告异常:{}'.", url + " | " +response, e);
//                        }
//                    }
//                    LogUtils.info("获取Xiaomi数据概览分析报告-所有游戏拉取完成");
//                }
//            }
//        }
//    }
//
//    /**
//     * 计算ViVo广告位报告均展数据
//     * https://adnet.vivo.com.cn/admin/report/ad
//     * https://adnet.vivo.com.cn/api/report/getReportTableData
//     */
//    public void updateAdData(int platform, GamesSimpleVo game, Date date, String dayString, ReportGameDay reportGameDay) {
//        //创建报告相关表
//        //匹配当前时间对应的表名, report_vivo_game_day_202410, 根据月份拆分表
//        //Date nowDateMM = DateUtils.parseDate(DateUtils.getDateMM());
//        //匹配昨天时间对应的表名
//        Date nowDateMM = DateUtils.parseDate(DateHandleUtil.getDataTimeYYMM(1));
//        //广告位报告表
//        //String tableReportAdName = TABLE_NAME_REPORT_AD_DAY_PREFIX + DateUtils.dateTimeMM(nowDateMM).replaceAll("-","");
//        String tableReportAdName = TABLE_NAME_REPORT_AD_DAY_PREFIX + dayString.substring(0, dayString.length() - 2);
//
//        LogUtils.info("计算Xiaomi广告位报告均展数据-当前游戏:", game.getGameName() + " | " + game.getGameId());
//
//        //匹配已有广告位报告数据报告表
//        ReportAdMediaDay report = new ReportAdMediaDay();
//        report.setGameId(game.getGameId()); //渠道游戏ID
//        report.setOnlinePlatformType(platform); //游戏上架平台(0:vivo, 1:oppo, 2:xiaomi, 3:huawei, 4:other)
//        report.setReportDate(date);//日期
//        report.setTableName(tableReportAdName);
//
//        //该游戏活跃用户数
//        double userActiveUserUv;
//        if(reportGameDay.getUserActiveUserUv()!=null && reportGameDay.getUserActiveUserUv()>0){
//            userActiveUserUv = reportGameDay.getUserActiveUserUv();
//        } else {
//            userActiveUserUv = 0;
//        }
//        //查询广告位报告数据列表
//        List<ReportAdMediaDay> reportAdMediaDayList = reportAdMediaDayMapper.selectReportAdMediaDayList(report);
//        if(reportAdMediaDayList !=null && !reportAdMediaDayList.isEmpty()){
//            for (ReportAdMediaDay reportAdMediaDay : reportAdMediaDayList) {
//                //计算ViVo广告位报告均展数据
//                reportAdMediaDay.setUserActiveUserUv(userActiveUserUv);
//                if(reportAdMediaDay.getReportAppView()!=null && reportAdMediaDay.getReportAppView()>0
//                        && reportAdMediaDay.getUserActiveUserUv()!=null && reportAdMediaDay.getUserActiveUserUv()>0){
//                    Double reportAverageView = NumberUtil.div(reportAdMediaDay.getReportAppView(), reportAdMediaDay.getUserActiveUserUv(), 4);
//                    reportAdMediaDay.setReportAverageView(reportAverageView); //广告位均展(展现量/活跃用户数)
//                }
//
//                reportAdMediaDay.setTableName(tableReportAdName);
//                // 执行更新
//                reportAdMediaDayMapper.updateReportAdMediaDay(reportAdMediaDay);
//            }
//
//            //计算每个广告位类型数据
//
//            // 系统开屏-2 横幅-3 插屏-4 原生-5 激励视频-9
//            //插屏->半插/全插
//            //   ->半插 半插-3  插屏半屏  插屏-半屏竖版/ 全插 全插-3  插屏全屏 插屏-全屏竖版
//            //原生->原生横幅/原生插屏/原生自渲染（oppo）/原生自渲染横幅（oppo）
//            //   ->原生-上图下文/原生-左图右文/原生模板-插屏-横幅/原生模板渲染-插屏-横幅/原生插屏上文下图/原生模板-插屏和横幅
//            //   原生模板横幅->原生横幅，其余均当原生插屏
//
//            // 根据广告位类型对List进行分组
//            Map<Integer, List<ReportAdMediaDay>> reportAdPlaceTypeList =
//                    reportAdMediaDayList.stream().collect(Collectors.groupingBy(ReportAdMediaDay::getReportAdPlaceType));
//            reportAdPlaceTypeList.forEach((adPlaceType, list) -> {
//                //计算该广告类型的所有展示数
//                double totalReportAppView = list.stream()
//                        .mapToDouble(ReportAdMediaDay::getReportAppView)
//                        .sum();
//                //计算该广告类型的所有收入
//                double totalReportAppIncome = list.stream()
//                        .mapToDouble(ReportAdMediaDay::getReportAppIncome)
//                        .sum();
//                //计算该广告类型的均展(展现量/活跃用户数)
//                double reportAverageView = NumberUtil.div(totalReportAppView, userActiveUserUv, 4);
//
//                // 匹配标签  系统开屏:0 横幅:1 半插:2 原生模板:3 激励视频:4 失败:5 原生模板横幅:6 全插:7 原生自渲染:8 原生自渲染横幅:9
//                if(adPlaceType==0){
//                    reportGameDay.setReportAppViewAdScreen(totalReportAppView);
//                    reportGameDay.setReportAppIncomeScreen(totalReportAppIncome);
//                    reportGameDay.setReportAverageViewAdScreen(reportAverageView);
//                } else if(adPlaceType==1){
//                    reportGameDay.setReportAppViewAdBanner(totalReportAppView);
//                    reportGameDay.setReportAppIncomeBanner(totalReportAppIncome);
//                    reportGameDay.setReportAverageViewAdBanner(reportAverageView);
//                } else if(adPlaceType==4){
//                    reportGameDay.setReportAppViewAdVideo(totalReportAppView);
//                    reportGameDay.setReportAppIncomeVideo(totalReportAppIncome);
//                    reportGameDay.setReportAverageViewAdVideo(reportAverageView);
//                } else if(adPlaceType==2){
//                    //插屏->半插/全插
//                    //   ->半插 半插-3  插屏半屏  插屏-半屏竖版/ 全插 全插-3  插屏全屏 插屏-全屏竖版
//                    //插屏->半插
//                    reportGameDay.setReportAppViewAdInsert(totalReportAppView);
//                    reportGameDay.setReportAppIncomeInsert(totalReportAppIncome);
//                    reportGameDay.setReportAverageViewAdInsert(reportAverageView);
//                } else if(adPlaceType==7){
//                    //插屏->全插
//                    reportGameDay.setReportAppViewAdInsertAll(totalReportAppView);
//                    reportGameDay.setReportAppIncomeInsertAll(totalReportAppIncome);
//                    reportGameDay.setReportAverageViewAdInsertAll(reportAverageView);
//                }
//            });
//
//            List<ReportAdMediaDay> reportAdMediaDaysOriginBannerList = new ArrayList<>();
//            List<ReportAdMediaDay> reportAdMediaDaysOriginInsertList = new ArrayList<>();
//            // 匹配标签  原生横幅-6:原生模板横幅 9:原生自渲染横幅   原生插屏-8:原生自渲染 3:原生模板
//            if(reportAdPlaceTypeList.containsKey(6) && reportAdPlaceTypeList.get(6)!=null) {
//                reportAdMediaDaysOriginBannerList.addAll(reportAdPlaceTypeList.get(6));
//            }
//            if(reportAdPlaceTypeList.containsKey(9) && reportAdPlaceTypeList.get(9)!=null) {
//                reportAdMediaDaysOriginBannerList.addAll(reportAdPlaceTypeList.get(9));
//            }
//            if(reportAdPlaceTypeList.containsKey(8) && reportAdPlaceTypeList.get(8)!=null) {
//                reportAdMediaDaysOriginInsertList.addAll(reportAdPlaceTypeList.get(8));
//            }
//            if(reportAdPlaceTypeList.containsKey(3) && reportAdPlaceTypeList.get(3)!=null) {
//                reportAdMediaDaysOriginInsertList.addAll(reportAdPlaceTypeList.get(3));
//            }
//
//            if(!reportAdMediaDaysOriginBannerList.isEmpty()){
//                double adAppView = reportAdMediaDaysOriginBannerList.stream()
//                        .mapToDouble(ReportAdMediaDay::getReportAppView)
//                        .sum();
//                //计算该广告类型的所有收入
//                double adAppIncome = reportAdMediaDaysOriginBannerList.stream()
//                        .mapToDouble(ReportAdMediaDay::getReportAppIncome)
//                        .sum();
//                //计算该广告类型的均展(展现量/活跃用户数)
//                double adAverageView = NumberUtil.div(adAppView, userActiveUserUv, 4);
//                //原生->原生横幅
//                reportGameDay.setReportAppViewAdOriginBanner(adAppView);
//                reportGameDay.setReportAppIncomeOriginBanner(adAppIncome);
//                reportGameDay.setReportAverageViewAdOriginBanner(adAverageView);
//            }
//            if(!reportAdMediaDaysOriginInsertList.isEmpty()){
//                double adAppView1 = reportAdMediaDaysOriginInsertList.stream()
//                        .mapToDouble(ReportAdMediaDay::getReportAppView)
//                        .sum();
//                //计算该广告类型的所有收入
//                double adAppIncome1 = reportAdMediaDaysOriginInsertList.stream()
//                        .mapToDouble(ReportAdMediaDay::getReportAppIncome)
//                        .sum();
//                //计算该广告类型的均展(展现量/活跃用户数)
//                double adAverageView1 = NumberUtil.div(adAppView1, userActiveUserUv, 4);
//                //原生->原生插屏
//                reportGameDay.setReportAppViewAdOriginInsert(adAppView1);
//                reportGameDay.setReportAppIncomeOriginInsert(adAppIncome1);
//                reportGameDay.setReportAverageViewAdOriginInsert(adAverageView1);
//            }
//        }
//    }
//
//}