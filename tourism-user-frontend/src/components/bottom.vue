<template>
</template>

<script>
  export default {
    data() {
      return{

      }
    },
    methods: {
      toPage(name) {
        if (name == "index") {
          this.$router.push("/")
        } else if (name == "vegetable") {
          this.$router.push("/vegetable")
        } else if (name == "information") {
          this.$router.push("/information")
        } else if (name == "shed") {
          this.$router.push("/shed")
        } else if (name == "leave") {
          this.$router.push("/leave")
        } else if (name == "manage") {
          window.open("http://localhost:3000")
        } else if (name == "car") {
          this.$router.push("/car")
        } else if (name == "favor") {
          this.$router.push("/favor")
        } else if (name == "order") {
          this.$router.push("/order")
        } else if (name == "appointment") {
          this.$router.push("/appointment")
        }
      }
    },
 }
</script>

<style scoped>
  .bottom {
      margin-top: 20px;
      width: 100%;
      height: 300px;
      display: flex;
      justify-content: space-around;
      background-color: #89A5C2;
      align-items: center;
      font-family: '黑体';
  }
  .bottom1,.bottom3,.bottom2 {
    color: #000000;
  }
  .bottom2 {
    width: 500px;
    letter-spacing: 2px;
    line-height: 30px;
  }
  .bottom1>div,.bottom3>div,.bottom2>div {
    font-size: 14px;
    margin-top: 12px;
    cursor: pointer;
  }
</style>
