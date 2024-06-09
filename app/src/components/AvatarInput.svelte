<script lang="ts">
	import { Input } from "flowbite-svelte";

    export let file
    export let onChange: (e: Event) => void = () => {}
</script>

<label for="avatar-input" class="pointer">
    Upload avatar
</label>
<Input id="avatar-input" on:change={onChange} class="hidden" type="file"/>

<style lang="scss">
    label {
        font-size: 12px;
        font-weight: 500;
        color: #cfcece;
        border: solid 1px #cfcece;
        padding: 8px 12px;
        border-radius: 7px;
        cursor: pointer;
    }
</style>