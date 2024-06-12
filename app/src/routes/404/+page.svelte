<div
	class="wrapper column absolute inset-0 m-auto h-screen max-h-[400px] w-screen max-w-[600px] overflow-hidden"
>
	<h1 class="text-[200px] font-black ">404</h1>
	<p class="font-black text-4xl text-primary-600">Page not found</p>
</div>

<svelte:head>
	<style>
		body {
			background: rgb(34, 162, 195);
			background: linear-gradient(0deg, #0a203c 0%, transparent 100%);
			min-height: 100vh;
		}
	</style>
</svelte:head>

<style>
	h1 {
		background: -webkit-linear-gradient(#fff, #484848);
		-webkit-background-clip: text;
        background-clip: text;
		-webkit-text-fill-color: transparent;
		animation: spin;
		animation-duration: 5s;
		animation-iteration-count: infinite;
		transform: skewY(8deg);
	}

	@keyframes spin {
		50% {
			transform: skewY(-8deg);
		}
	}
</style>
