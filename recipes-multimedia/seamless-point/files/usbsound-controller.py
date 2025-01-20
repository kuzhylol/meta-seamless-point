import argparse
import evdev
import subprocess
import asyncio

async def volume_up(hwindex):
    """Increase the volume."""
    print("Volume up")
    try:
        subprocess.call(['amixer', '-c', hwindex, 'sset', 'Headphone', '10%+'])
    except subprocess.CalledProcessError as e:
        print(f"Error increasing volume: {e}")

async def volume_down(hwindex):
    """Decrease the volume."""
    print("Volume down")
    try:
        subprocess.call(['amixer', '-c', hwindex, 'sset','Headphone', '10%-'])
    except subprocess.CalledProcessError as e:
        print(f"Error decreasing volume: {e}")

async def handle_events(device, hwindex):
    """Handle input events from the given device."""
    async for event in device.async_read_loop():
        if event.type == evdev.ecodes.EV_KEY and event.value == 1:  # Key press
            if event.code == evdev.ecodes.KEY_PLAYPAUSE:
                 print("Pause is not implemented")
            elif event.code == evdev.ecodes.KEY_VOLUMEUP:
                await volume_up(hwindex)
            elif event.code == evdev.ecodes.KEY_VOLUMEDOWN:
                await volume_down(hwindex)
            else:
                print(f"Unhandled key event: {event.code}")

async def main():
    """Main function to monitor the device."""
    parser = argparse.ArgumentParser(description="USB sound controller")

    parser.add_argument("-i", "--hwindex", type=int, choices=[0, 1, 2], default=2, help="Sound Card index")
    args = parser.parse_args()

    device_path = "/dev/input/usbsound_control_event0"

    while True:
        try:
            # Open the device
            device = evdev.InputDevice(device_path)
            print(f"Monitoring device: {device.name} at {device.path}")

            # Handle events asynchronously
            await handle_events(device, str(args.hwindex))

        except FileNotFoundError:
            print(f"Error: Device {device_path} not found.")
        except PermissionError:
            print(f"Error: Insufficient permissions to access {device_path}.")
        except Exception as e:
            print(f"Unexpected error: {e}")


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("Script terminated.")

