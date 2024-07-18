<template>
  <div class="footer">
    <h3 style="margin-top: 50px">别往下翻了哦~~~~下面没有啦</h3>
    <div class="footer-logo">
      <img src="@/assets/images/down.png" alt="">
      <p>xia的购物商城~</p>
    </div>

    <div class="copyright">Copyright ©2023 <span class="domain">xia的购物商城</span> All Rights Reserved.</div>
  </div>
</template>

<script>
export default {
  name: "NavFooter"
}
</script>

<style scoped>
.footer{
  height:234px;
  border-top:4px solid #FF6600;
  background-color:#333333;
  color:#999999;
  font-size:16px;
  text-align:center;
  .footer-logo{
    margin-top:46px;
    margin-bottom:31px;
    img{
      width:53px;
      height:53px;
    }
  }
  .footer-link {
    a{
      color:#999999;
      display:inline-block;
    }
    span{
      margin:0 10px;
    }
    margin-bottom:13px;
  }
  .copyright{
    .domain{
      color:#FF6600;
    }
  }
}
</style>