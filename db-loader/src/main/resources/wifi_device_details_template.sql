select w.device_name,
    w.mac_address,
    d.readable_name,
    d.type,
    d.owner,
    count(*)
from wifi_connection w
    full outer join wifi_device_details d ON w.mac_address = d.mac_address
group by w.device_name,
    w.mac_address,
    d.readable_name,
    d.type,
    d.owner
order by count(*) desc;