<template>
  <div>

    <div v-if="suffix==='obj'">
      <model-obj :src="src"></model-obj>
    </div>

    <div v-else-if="suffix==='json'">
      <model-three :src="src"></model-three>
    </div>

    <div v-else-if="suffix==='ifc'">
      <model-ifc :src="src"></model-ifc>
    </div>

    <div v-else-if="suffix==='fbx'">
      <model-fbx :src="src"></model-fbx>
    </div>

    <div v-else-if="suffix==='stl'">
      <model-stl :src="src"></model-stl>
    </div>

    <div v-else-if="suffix==='dae'">
      <model-collada :src="src"></model-collada>
    </div>

    <div v-else-if="suffix==='ply'">
      <model-ply :src="src"></model-ply>
    </div>

    <div v-else-if="suffix==='gltf'">
      <model-gltf :src="src"></model-gltf>
    </div>

    <div v-else-if="suffix==='glb'">
      <model-gltf :src="src"></model-gltf>
    </div>

    <div id="msg"></div>
  </div>
</template>

<script>

import threedviewerApi from '@/api/threedviewer'
import ModelFbx from "@/components/Model/model-fbx.vue";
import ModelGltf from "@/components/Model/model-gltf.vue";
import ModelObj from "@/components/Model/model-obj.vue";
import ModelThree from "@/components/Model/model-three.vue";
import ModelStl from "@/components/Model/model-stl.vue";
import ModelCollada from "@/components/Model/model-collada.vue";
import ModelPly from "@/components/Model/model-ply.vue";


export default {

  components: {
    ModelFbx, ModelGltf, ModelObj, ModelThree, ModelStl, ModelCollada, ModelPly
  },
  data(){
    return {
      name: '',
      src:'',
      suffix:''
    }
  },
  mounted(){
    let vm = this
    let filePath = JSON.parse(vm.$route.query.filePath.toString())
    console.log('读取文件' + filePath)
    let success = false
    if (filePath) {
      for (let filePathElement of filePath) {
        let suffix = filePathElement.name.split('.').pop()
        if(['obj','json','ifc','fbx','stl','dae','ply','gltf','glb'].includes(suffix)) {
          vm.src = filePathElement.url
          vm.name = filePathElement.name
          vm.suffix = suffix
          success = true
          break
        }
      }
    }

    if(!success) {
      let msg = document.getElementById('msg')
      msg.innerHTML='文件格式并非obj，json，ifc，fbx，stl，dae，ply，gltf，glb，因此无法预览'
    }
  }
}

</script>


<style lang="scss" scoped>

</style>
