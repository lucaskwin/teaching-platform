<template>
  <div>

<!--    <el-row class="echarts-line">-->
<!--    -->
<!--   -->
      <div id="echarts-graph"  style="width: 100%;height:90vh;" v-loading="loading"/>
<!--    </el-row>-->
  </div>
</template>

<script>


export default {
  data(){
    return {
      echartsGraph: null,
      loading:false
    }
  },
  mounted(){
    this.echartsGraph = echarts.init(document.getElementById('echarts-graph'), 'macarons')
    this.loading = true
    this.echartsGraph.setOption(
      {
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 20,
          bottom: 20,
          // data: categories.map(function (a) {
          //   return a.name;
          // })
        },
        series: [
          {
            type: 'graph',
            layout: 'force',
            force: {
              repulsion: 1000
            },
            data: [
              {
                name: 'Node 1',
                x: 300,
                y: 300
              },
              {
                name: 'Node 2',
                x: 800,
                y: 300
              },
              {
                name: 'Node 3',
                x: 550,
                y: 100
              },
              {
                name: 'Node 4',
                x: 550,
                y: 500
              }
            ],
            links: [{
              source: 0,
              target: 1,
              symbolSize: [5, 20],
              label: {
                show: true
              },
              lineStyle: {
                width: 5,
                curveness: 0.2
              }
            },
              {
                source: 'Node 2',
                target: 'Node 1',
                label: {
                  show: true
                },
                lineStyle: {
                  curveness: 0.2
                }
              },
              {
                source: 'Node 1',
                target: 'Node 3'
              },
              {
                source: 'Node 2',
                target: 'Node 3'
              },
              {
                source: 'Node 2',
                target: 'Node 4'
              },
              {
                source: 'Node 1',
                target: 'Node 4'
              }],
            // categories: categories,
            roam: true,
            label: {
              show: true,
              formatter:'{c}'
            },
            labelLayout: {
              hideOverlap: true
            },
            lineStyle: {
              color: 'source',
              curveness: 0
            }
          }
        ]
      }
    )
    this.loading = false
  }
}

</script>


<style lang="scss" scoped>

</style>
