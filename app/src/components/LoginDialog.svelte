<script>
	import { updateDialog } from '$lib/dialogStore';
	import { Input, InputAddon, ButtonGroup, Button, Helper } from 'flowbite-svelte';
	import { UserCircleSolid, LockSolid, LinkOutline } from 'flowbite-svelte-icons';
	import { useForm, required, minLength, maxLength } from 'svelte-use-form';

	const iconStyles =
		'h-4 w-4 text-gray-500 dark:text-gray-400 h-4 w-4 text-gray-500 dark:text-gray-400';
	const errorMessagesStyles = 'mt-2 font-medium';

	const form = useForm({
		username: { validators: [required, minLength(3), maxLength(22)] },
		password: { validators: [required, minLength(6), maxLength(50)] }
	});
</script>

<div class="dialog authDialog column">
	<div class="row mt-2">
		<LinkOutline size="xl" color="pink" />
		<i class="ml-2 text-3xl font-semibold text-white">Links.io</i>
	</div>
	<h1 class="text-md mb-8 text-white">Welcome back</h1>
	<form use:form class="w-full">
		<div class="w-full">
			<ButtonGroup class="w-full">
				<InputAddon>
					<UserCircleSolid class={iconStyles} />
				</InputAddon>
				<Input placeholder="username" name="username" autocomplete="username" />
			</ButtonGroup>
		</div>
		{#if !$form.username.valid && $form.username._touched}
			<Helper class={errorMessagesStyles} color="red"
				>Username should be between 3 and 22 characters</Helper
			>
		{/if}
		<div class="w-full">
			<ButtonGroup class="mt-5 w-full">
				<InputAddon>
					<LockSolid class={iconStyles} />
				</InputAddon>
				<Input placeholder="password" name="password" autocomplete="password" type="password" />
			</ButtonGroup>
		</div>
		{#if !$form.password.valid && $form.password._touched}
			<Helper class={errorMessagesStyles} color="red"
				>Password should be between 6 and 50 characters</Helper
			>
		{/if}
		<Button disabled={!$form.valid} class="mr-[auto] mt-5 w-full">Sign in</Button>
	</form>
	<button on:click={() => updateDialog('registration')} class="mt-4 text-sm text-white">
		New here? Sign up now!
	</button>
	<p class="my-4 text-xs font-medium text-gray-400">Or Sign in with</p>
	<Button outline color="dark">
		<div class="row w-[120px] justify-center">
			<img src="google_logo.webp" alt="Google logo" />
			Google
		</div>
	</Button>
</div>

<style>
	img {
		width: 18px;
		margin-right: 10px;
	}
</style>
