<template>
  <div>
    <!--    上面的搜索栏-->
    <div>
      <h1 class="topLogs"><a href="/shop"><img src="@/assets/images/xia-shop.png" alt="回到首页"></a></h1>
      <div style="margin: 50px">
        <el-input @keydown.enter.native="loadData" size="big" v-model="search" placeholder="请输入你搜索的商品关键词"
                  style="width: 40%;margin-left: 100px" clearable></el-input>
        <el-button type="danger" style="margin-left: 25px" icon="el-icon-search" size="big" @click="loadData">搜索
        </el-button>
      </div>
    </div>
    <div class="button-container">
      <el-button type="primary" @click="fillSearchBox1">计算机</el-button>
      <el-button type="primary" @click="fillSearchBox2">衣服</el-button>
      <el-button type="primary" @click="fillSearchBox3">手机</el-button>
      <el-button type="primary" @click="fillSearchBox4">水乳</el-button>
      <el-button type="primary" @click="sortItemsBy('taobaoPrice')">按价格排序</el-button>
      <el-button type="primary" @click="sortItemsBy('qualityScore')">按评分排序</el-button>
    </div>
    <br>

    <!--    下面的卡片视图-->
    <div style="background-color: white;margin: 0 200px">
      <el-row>
        <el-col :span="5" v-for="item in itemData" :key="item.id" :offset="2">
          <!--        用a标签包裹的卡片装商品，给href绑定动态地址-->
          <a :href="'/shop/detail?id='+item.id">
            <el-card :body-style="{ padding: '5px'}">
              <img :src="item.image?item.image:hamburgUrl"
                   class="image" alt="缺少图片">
              <div style="padding: 10px;">
                <span class="title">{{ item.title }}</span>
                <div class="info-wrapper">
                  <div class="sellPoint">{{ item.sellPoint }}</div>
                  <div><i class="el-icon-s-goods" style="float: right;color: rgba(255,80,24);font-size: 20px">
                    $:{{ item.taobaoPrice }}</i></div>
                </div>
              </div>
            </el-card>
          </a>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "ShopView",
  data() {
    return {
      sortOrder: '',
      // 查询的关键词
      search: '',
      // 收到的商品数据
      itemData: [],
      //汉堡Url
      hamburgUrl: "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
    };
  },
  created() {
    this.loadData();
    document.title = "Xia-Shop"
  },
  computed: {
    sortedItemData() {
      if (this.sortOrder === 'qualityScore') {
        return this.itemData.slice().sort((a, b) => b.qualityScore - a.qualityScore); // 根据评分降序排序
      } else if (this.sortOrder === 'taobaoPrice') {
        return this.itemData.slice().sort((a, b) => a.taobaoPrice - b.taobaoPrice);
      } else {
        return this.itemData;
      }
    }
  },
  methods: {
    fillSearchBox1() {
      // 点击按钮填充搜索框的逻辑
      this.search = '计算机';
      request.get('/item/getAll',{
        params: {
          search: this.search,
        }
      }).then(res => {
        this.itemData = res.data;
      })
    },
    fillSearchBox2() {
      this.search = '衣服';
      request.get('/item/getAll',{
        params: {
          search: this.search,
        }
      }).then(res => {
        this.itemData = res.data;
      })
    },
    fillSearchBox3() {
      this.search = '手机';
      request.get('/item/getAll',{
        params: {
          search: this.search,
        }
      }).then(res => {
        this.itemData = res.data;
      })
    },
    fillSearchBox4() {
      this.search = '水乳';
      request.get('/item/getAll',{
        params: {
          search: this.search,
        }
      }).then(res => {
        this.itemData = res.data;
      })
    },
    sortItemsBy(key) {
      this.sortOrder = key;
      if (key === 'qualityScore') {
        this.itemData.sort((a, b) => b.qualityScore - a.qualityScore); // 根据评分降序排序
      } else {
        this.itemData.sort((a, b) => a.taobaoPrice - b.taobaoPrice);
      }
    },
    loadData() {
      request.get('/item/getAll',{
        params: {
          search: this.search,
        }
      }).then(res => {
        this.itemData = res.data;
      })
    }
  }
}
</script>

<style scoped>
.title{
  font-size: 15px;
}
.title:hover{
  color: #ff5000;
}
.info-wrapper .sellPoint {
  display: inline-block;
  height: 46px;
  margin: 6px 0 2px 0;
  overflow: hidden;
  color: #333;
  font-size: 10px;
  line-height: 23px;
}

.info-wrapper .sellPoint:hover {
  color: #ff5000;
}

.image {
  width: 100%;
  height: 223px;
}

.topLogs {
  margin-left: 200px;
  margin-top: -30px;
  float: left;
}

.topLogs img {
  width: 200px;
  height: 72px;
}

.button-container {
  display: flex;
  justify-content: center; /* 水平居中 */
}
.button-container .el-button {
  background-color: orange; /* 设置橙色背景 */
  color: white; /* 设置文字颜色为白色 */
  margin: 0 10px; /* 按钮之间的间距 */
}
</style>