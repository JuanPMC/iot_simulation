/**
 * A model for managing members in groups.
 */
function create_members_model(groups) {
	// create the data structure
	var all_members = new Set(); // all unique member names
	var group_names = [];
	var group_members = new Map(); // group_name to set of group members
	var memeber_state = new Map();

	for (var group of groups) {
		group_names.push(group.name);
		var members = new Set(group.members);
		let member_names_priv = new Set();
		members.forEach(member => all_members.add(member.name));
		members.forEach(member => member_names_priv.add(member.name));
		members.forEach(member => memeber_state.set(member.name,member));
		group_members.set(group.name, member_names_priv);
	}
	var member_names = Array.from(all_members);
	group_names.sort();
	member_names.sort();

	// create the object
	var that = {}
	that.get_group_names = () => group_names;
	that.get_member_names = () => member_names;
	that.is_member_in_group = (member_name, group_name) =>
		!group_members.has(group_name)? false: group_members.get(group_name).has(member_name);
	that.get_group_members = group_name => group_members.get(group_name);
	that.get_state = (member_name) => memeber_state.get(member_name);

	console.debug("Members Model", groups, group_names, member_names, group_members);

	return that;
}

/**
 * The Members controller holds the state of groups.
 * It creates its view in render().
 */
class Members extends React.Component {

	constructor(props) {
		super(props);
		console.info("Members constructor()");
		this.state = {
			members: create_members_model([]),
			inputName: "",
			inputMembers: "",
		};
	}

	componentDidMount() {
		console.info("Members componentDidMount()");
		this.getGroups();
		setInterval(this.getGroups, 1000);
	}

	render() {
		//console.info("Members render()");
		return (<MembersTable members={this.state.members}
			inputName={this.state.inputName} inputMembers={this.state.inputMembers}
			onMemberChange={this.onMemberChange}
			onDeleteGroup={this.onDeleteGroup}
			onInputNameChange={this.onInputNameChange}
			onInputMembersChange={this.onInputMembersChange}
			onAddGroup={this.onAddGroup}
			onAddMemberToAllGroups={this.onAddMemberToAllGroups}
			removeMemberFromAllGroups={this.removeMemberFromAllGroups}
			toggleGroup={this.toggleGroup}
			onGroup={this.onGroup}
			offGroup={this.offGroup}
			get_group_members={this.get_group_members}
			 />);
	}

	getGroups = () => {
		console.debug("RESTful: get groups");
		fetch("api/groups")
			.then(rsp => rsp.json())
			.then(groups => this.showGroups(groups))
			.catch(err => console.error("Members: getGroups", err));
	}

	showGroups = groups => {
		this.setState({
			members: create_members_model(groups)
		});
	}

	createGroup = (groupName, groupMembers) => {
		console.info("RESTful: create group "+groupName
			+" "+JSON.stringify(groupMembers));
		
		var postReq = {
			method: "POST",
			headers: {"Content-Type": "application/json"},
			body: JSON.stringify(groupMembers)
		};
		fetch("api/groups/"+groupName, postReq)
			.then(rsp => this.getGroups())
			.catch(err => console.error("Members: createGroup", err));
	}

	createManyGroups = groups => {
		console.info("RESTful: create many groups "+JSON.stringify(groups));
		var pendingReqs = groups.map(group => {
			var postReq = {
				method: "POST",
				headers: {"Content-Type": "application/json"},
				body: JSON.stringify(group.members)
			};
			return fetch("api/groups/"+group.name, postReq);
		});

		Promise.all(pendingReqs)
			.then(() => this.getGroups())
			.catch(err => console.error("Members: createManyGroup", err));
	}

	deleteGroup = groupName => {
		console.info("RESTful: delete group "+groupName);
	
		var delReq = {
			method: "DELETE"
		};
		fetch("api/groups/"+groupName, delReq)
			.then(rsp => this.getGroups())
			.catch(err => console.error("Members: deleteGroup", err));
	}

	onMemberChange = (memberName, groupName) => {
		var groupMembers = new Set(this.state.members.get_group_members(groupName));

		if (groupMembers.has(memberName))
			groupMembers.delete(memberName);
		else
			groupMembers.add(memberName);

		this.createGroup(groupName, Array.from(groupMembers));
	}

	onDeleteGroup = groupName => {
		this.deleteGroup(groupName);
	}

	onInputNameChange = value => {
		console.debug("Members: onInputNameChange", value);
		this.setState({inputName: value});
	}

	onInputMembersChange = value => {
		console.debug("Members: onInputMembersChange", value);
		this.setState({inputMembers: value});
	}

	onAddGroup = () => {
		var name = this.state.inputName;
		var members = this.state.inputMembers.split(',');
	
		this.createGroup(name, members);
	}

	onAddMemberToAllGroups = memberName => {
		var groups = [];
		for (var groupName of this.state.members.get_group_names()) {
			var groupMembers = new Set(this.state.members.get_group_members(groupName));
			groupMembers.add(memberName);
			groups.push({name: groupName, members: Array.from(groupMembers)});
		}
		this.createManyGroups(groups);
	}

	removeMemberFromAllGroups = memberName => {
		var groups = [];
		for (var groupName of this.state.members.get_group_names()) {
			var groupMembers = new Set(this.state.members.get_group_members(groupName));
			groupMembers.delete(memberName);
			groups.push({name: groupName, members: Array.from(groupMembers)});
		}
		this.createManyGroups(groups);
	}


	plugAction = (name, action) => {
		var url = "../api/plugs/" + name + "?action=" + action;
		console.info("PlugDetails: request " + url);
		fetch(url);
	}

	toggleGroup = groupMembers => {
		for (var member of groupMembers){
			this.plugAction(member,"toggle");
		}
	}
	onGroup = groupMembers => {
		for (var member of groupMembers){
			this.plugAction(member,"on");
		}
	}
	offGroup = groupMembers => {
		for (var member of groupMembers){
			this.plugAction(member,"off");
		}
	}
}

// export
window.Members = Members;