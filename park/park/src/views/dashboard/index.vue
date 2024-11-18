<template>
  <div class="app-container">
    <div>
      <div id="myChart" style="width: 100%;height:600px;margin-top: 10px; float: left"></div>
    </div>
  </div>
</template>

<script>
import { chart } from '@/api/sys/inOut'
export default {
  name: 'Dashboard',
  data() {
    return {
    }
  },
  mounted(){
    this.drawLine();
  },
  methods: {
    drawLine(){
      chart().then(res => {
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById('myChart'))
        // 绘制图表
        myChart.setOption({
          color: ['#3398DB'],
          title: {
            text: '停车场缴费收入统计',
            subtext: '单位：元',
            left: 'center'
          },

          tooltip: {
            trigger: 'axis',//坐标轴触发
            axisPointer: {
              type: 'shadow'
            }
          },
          //tooltip的作用是，当鼠标放在柱状图上时，会显示出该柱状图的数据
          xAxis: {
            data: res.data.names
          },
          yAxis: {},
          series: [{
            name: '缴费收入',
            type: 'bar',//bar是柱状图
            data: res.data.nums
          }],
          //series是一个数组，数组中的每一个对象就是一个柱状图

          animationType: 'scale',//放大显示
          animationEasing: 'elasticOut',//弹性放大
          animationDelay: function (idx) {
            return Math.random() * 200;
          }
        });
      });

    }
  }
}
</script>

<style lang="scss" scoped>

</style>
