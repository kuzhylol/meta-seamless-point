import sys
import re

CONFIG_FILE = "/etc/asound.conf"

TEMPLATE = """
pcm.{mac_lower} {{
    type plug
    slave.pcm {{
        type bluealsa
        device "{mac_upper}"
        profile "a2dp"
    }}
    hint {{
        show on
        description "Bluetooth Speaker"
    }}
}}
"""

def read_config():
    """Reads the asound.conf file."""
    try:
        with open(CONFIG_FILE, "r") as f:
            return f.read()
    except FileNotFoundError:
        return ""

def write_config(content):
    """Writes the new configuration to asound.conf."""
    with open(CONFIG_FILE, "w") as f:
        f.write(content)

def add_mac(mac):
    """Adds a new PCM node if it does not already exist."""
    mac_lower = mac.lower().replace(":", "")
    mac_upper = mac.upper()

    config = read_config()
    if f'pcm.{mac_lower} ' in config:
        print(f"MAC {mac} already exists in {CONFIG_FILE}. No changes made.")
        return

    new_entry = TEMPLATE.format(mac_lower=mac_lower, mac_upper=mac_upper)
    new_config = config.strip() + "\n" + new_entry.strip() + "\n"

    write_config(new_config)
    print(f"Added MAC {mac} to {CONFIG_FILE}.")

def remove_mac(mac):
    """Removes a PCM node by MAC address."""
    mac_lower = mac.lower().replace(":", "")

    config = read_config()
    pattern = rf"\n?pcm\.{mac_lower} \{{.*?\n\}}\n?"

    new_config = re.sub(pattern, "", config, flags=re.DOTALL)

    if new_config == config:
        print(f"MAC {mac} not found in {CONFIG_FILE}. No changes made.")
        return

    write_config(new_config.strip() + "\n")
    print(f"Removed MAC {mac} from {CONFIG_FILE}.")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python3 script.py add|remove MAC_ADDRESS")
        sys.exit(1)

    action, mac_address = sys.argv[1], sys.argv[2]

    if not re.match(r"([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}", mac_address):
        print("Invalid MAC address format.")
        sys.exit(1)

    if action == "add":
        add_mac(mac_address)
    elif action == "remove":
        remove_mac(mac_address)
    else:
        print("Invalid action. Use 'add' or 'remove'.")
