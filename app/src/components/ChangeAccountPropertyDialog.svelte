<script lang="ts">
    export let property: string;
	export let form;
	export let onSubmit;

	import { Button } from 'flowbite-svelte';
</script>

<div class="dialog authDialog column">
	<h1 class="text-xl font-bold mb-4 text-white">Change {property}</h1>
	<form use:form class="w-full" on:submit={e => e.preventDefault()}>
		<slot></slot>
		<Button disabled={!$form.valid} on:click={onSubmit} class="mr-[auto] mt-8 w-full">Confirm</Button>
	</form>
</div>
