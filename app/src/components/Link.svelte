<script lang="ts">
	export let text: string;
	export let url: string;

	import { LinkOutline } from "flowbite-svelte-icons";

</script>

<a href={url} class="row" target="_blank">
	<LinkOutline />
	<span class="mx-auto">{text}</span>
</a>

<style lang="scss">
	a {
		width: 80vw;
		max-width: 400px;
		height: 55px;
		outline: solid 1px pink;
		background-color: #ffffff08;
		margin: 18px 0;
		border-radius: 25px;
		justify-content: center;
		transition: 250ms ease-in-out;
		color: #fff;
		padding: 0 20px;
		box-sizing: border-box;

		&:hover {
			background-color: rgb(251, 220, 224);
			outline: solid 2px #000;
			color: #000;
			font-weight: 600;
		}
	}
</style>
