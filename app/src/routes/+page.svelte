<script lang="ts">
	import { Button, GradientButton } from 'flowbite-svelte';
	import { RocketOutline, UserOutline } from 'flowbite-svelte-icons';
	import { updateDialog } from '$lib/dialogStore';
</script>

<svelte:head>
	<style>
		body {
			background: radial-gradient(circle, rgba(89, 10, 217, 0.15) 0%, rgba(17, 24, 39, 1) 75%);
			background-repeat: no-repeat;
			min-height: 100vh;
		}
	</style>
</svelte:head>

<div class="column">
	<h1
		class="mt-4 max-w-[700px] px-4 text-center text-7xl font-black text-[#fdddc4] lg:text-[100px] lg:leading-[120px]"
	>
		One place for all <span>accounts</span>
	</h1>
	<p class="my-[12px] max-w-[250px] text-center text-[20px] font-light text-[#bcbaba]">
		One link on your profile for all social media links
	</p>
	<div class="row mt-6 flex-wrap justify-center space-x-4 px-2">
		<Button size="lg" on:click={() => updateDialog('registration')}>
			<RocketOutline ariaLabel="circle check solid" />
			<span class="ml-2">Sign up now!</span>
		</Button>
		<GradientButton outline size="lg" color="purpleToPink" on:click={() => updateDialog('login')}>
			<UserOutline />
			<span class="ml-2">Already member</span>
		</GradientButton>
	</div>
</div>

<style lang="scss">
	h1 span {
		-webkit-text-stroke: 1px var(--primary);
		-webkit-text-fill-color: transparent;
	}
</style>
