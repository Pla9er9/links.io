<script lang="ts">
	import { Input, Button, Textarea, Avatar, Helper } from 'flowbite-svelte';
	import {
		ArrowRightToBracketOutline,
		CheckOutline,
		LockOutline,
		MailBoxOutline,
		TrashBinOutline
	} from 'flowbite-svelte-icons';
	import { maxLength, minLength, required, useForm } from 'svelte-use-form';
	import LinksEditor from '$components/LinksEdittor.svelte';
	import FileInput from '$components/AvatarInput.svelte';
	import fetcher from '$lib/fetcher';
	import { toastStore, type toast } from '$lib/toastStore';
	import { get } from 'svelte/store';
	import { userStore } from '$lib/userStore';
	import { dialogStroe } from '$lib/dialogStore';
	import { PUBLIC_SERVER_URL } from '$env/static/public';

	export let data;

	const iconButtonTailwindClasses = 'm-2 space-x-2 w-full max-w-[320px] md:w-[180px]';
	const account = data.account;
	const token = get(userStore).token ?? undefined;

	let links = account.links;
	let file: File | null = null;
	let avatarSrc = `${PUBLIC_SERVER_URL}/profile/${data.username}/avatar`;

	const form = useForm({
		username: { validators: [required, minLength(3), maxLength(22)] },
		description: {}
	});

	async function onAvatarChange(event: Event) {
		// @ts-ignore
		const fileList: FileList = event.target!.files;
		const file: File | null = fileList.item(0);
		if (!file) {
			return;
		}

		const form = new FormData();
		form.append('file', file);

		const res = await fetcher('/account/avatar', {
			token: token,
			method: 'POST',
			noContentType: true,
			stringify: false,
			body: form
		});

		if (!res.ok) {
			toastStore.update((t) => {
				t.push({
					title: 'Action failed',
					text: 'Avatar upload failed, try later',
					variant: 'danger'
				});
				return t;
			});
			return;
		}

		account.hasAccountAvatar = true;
		avatarSrc = URL.createObjectURL(file);
	}

	async function deleteAvatar() {
		const res = await fetcher('/account/avatar', {
			token: token,
			method: 'DELETE'
		});

		if (!res.ok) {
			toastStore.update((t) => {
				t.push({
					title: 'Action failed',
					text: 'Could not delete avatar, try later',
					variant: 'danger'
				});
				return t;
			});
			return;
		}
		account.hasAccountAvatar = false;
	}

	async function saveChanges() {
		console.log({
			username: $form.username,
			description: $form.description,
			links: links
		});
		const res = await fetcher('/account/profile', {
			method: 'PUT',
			token: token,
			body: {
				username: $form.username.value,
				description: $form.description.value,
				links: links
			}
		});

		let newToast: toast;
		if (res.ok) {
			newToast = {
				title: 'Succes',
				text: 'Changes saved correctly',
				variant: 'succes'
			};
		} else {
			newToast = {
				title: 'Action failed',
				text: 'Changes could not be saved, try later',
				variant: 'danger'
			};
		}

		toastStore.update((t) => {
			t = [...t, newToast];
			return t;
		});
	}

	function changePassword() {
		dialogStroe.update(u => "changePassword")
	}

	function changeEmail() {
		dialogStroe.update(u => "changeEmail")
	}

	async function logout() {
		const res = await fetch('/api/auth/logout', {
			method: "POST"
		})

		if (!res.ok) {
			toastStore.update((t) => {
				const newToast: toast = {
					title: 'Action failed',
					text: 'Could not logout, try later',
					variant: 'danger'
				};
				t = [...t, newToast];
				return t;
			});
			return
		}

		location.replace("/")
	}

	async function deleteAccount() {
		const res = await fetcher('/account', {
			token: token,
			method: 'DELETE'
		});

		if (!res.ok) {
			toastStore.update((t) => {
				const newToast: toast = {
					title: 'Error',
					text: 'Could not delete account, try later',
					variant: 'danger'
				};
				t = [...t, newToast];
				return t;
			});
			return;
		}

		await logout()
	}
</script>

<div class="column mx-auto mt-8 max-w-[900px] space-y-4 px-8" style="align-items: start;">
	<h1 class="mb-4 text-2xl font-medium text-pink-200">Account settings</h1>
	<div class="row w-full">
		<Avatar rounded size="lg" src={account.hasAccountAvatar ? avatarSrc : ''}>
			<span class="text-2xl">{account.username.slice(0, 2)}</span>
		</Avatar>
		<div class="row ml-auto space-x-4">
			<FileInput bind:file onChange={onAvatarChange} />
			<Button outline disabled={avatarSrc === ''} color="red" size="xs" on:click={deleteAvatar}
				>Delete avatar</Button
			>
		</div>
	</div>
	<form use:form class="w-full space-y-4">
		<Input value={account.username} name="username" placeholder="Username" />
		{#if !$form.username.valid && $form.username._touched}
			<Helper color="red">Username should be between 3 and 22 characters</Helper>
		{/if}
		<Textarea
			value={account.description}
			maxLength={150}
			name="description"
			placeholder="Description"
			class="h-24 resize-none"
		/>
	</form>
	<LinksEditor bind:links />
	<div class="row mx-auto max-w-[screen] flex-wrap md:mx-0 md:max-w-full">
		<Button on:click={saveChanges} disabled={!$form.valid} class={iconButtonTailwindClasses}>
			<CheckOutline />
			<span>Confirm changes</span>
		</Button>
		<Button on:click={changePassword} class={iconButtonTailwindClasses} color="dark">
			<LockOutline />
			<span>Change Password</span>
		</Button>
		<Button on:click={changeEmail} class={iconButtonTailwindClasses} color="dark">
			<MailBoxOutline />
			<span>Change Email</span>
		</Button>
		<Button on:click={logout} class={iconButtonTailwindClasses} color="dark">
			<ArrowRightToBracketOutline />
			<span>Logout</span>
		</Button>
		<Button on:click={deleteAccount} class={iconButtonTailwindClasses} outline color="red">
			<TrashBinOutline />
			<span>Delete account</span>
		</Button>
	</div>
</div>
