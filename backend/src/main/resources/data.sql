INSERT OR IGNORE INTO  users (id, username, uuid) VALUES (1, 'Trailblazer', 'uuid-111');
INSERT OR IGNORE INTO users (id, username, uuid) VALUES (2, 'KafkaMain', 'uuid-222');
INSERT OR IGNORE INTO users (id, username, uuid) VALUES (3, 'BronyaEnjoyer', 'uuid-333');
INSERT OR IGNORE INTO relics (
    user_id, set_name, piece, slot, main_stat, main_value, image_path, timestamp
) VALUES
(1, 'Ever-Glorious Magical Girl', 'Shining Medal', 'Head', 'HP', '112', '/img1.webp', '2026-05-10T10:00:00Z'),

(1, 'Ever-Glorious Magical Girl', 'Everdance Skirt', 'Body', 'CRIT DMG', '10.3%', '/img2.webp', '2026-05-10T10:01:00Z'),

(1, 'Iron Cavalry Against the Scourge', 'Skywalk Greaves', 'Feet', 'SPD', '25', '/img3.webp', '2026-05-10T10:02:00Z'),

(2, 'Diviner of Distant Reach', 'Astral Robe', 'Body', 'Effect Hit Rate', '6.9%', '/img4.webp', '2026-05-10T10:03:00Z'),

(2, 'Hero of Triumphant Song', 'Gilded Bracers', 'Hand', 'ATK', '352', '/img5.webp', '2026-05-10T10:04:00Z'),

(2, 'Belobog of the Architects', 'Iron Defense Rope', 'Link Rope', 'DEF%', '54.0%', '/img6.webp', '2026-05-10T10:05:00Z'),

(3, 'Talia Kingdom of Banditry', 'Nailscrap Sphere', 'Planar Sphere', 'ATK%', '43.2%', '/img7.webp', '2026-05-10T10:06:00Z'),

(3, 'Lushaka Sunken Seas', 'Waterscape Sphere', 'Planar Sphere', 'Ice DMG Boost', '38.8%', '/img8.webp', '2026-05-10T10:07:00Z'),

(3, 'Prisoner in Deep Confinement', 'Sealed Muzzle', 'Head', 'HP', '705', '/img9.webp', '2026-05-10T10:08:00Z'),

(1, 'Guard of Wuthering Snow', 'Shining Gauntlets', 'Hand', 'ATK', '352', '/img10.webp', '2026-05-10T10:09:00Z'),

(2, 'Messenger Traversing Hackerspace', 'Holovisor', 'Head', 'HP', '705', '/img11.webp', '2026-05-10T10:10:00Z'),

(1, 'Pioneer Diver of Dead Waters', 'Waterproof Coat', 'Body', 'CRIT Rate', '32.4%', '/img12.webp', '2026-05-10T10:11:00Z'),

(2, 'The Ashblazing Grand Duke', 'Robe of Grace', 'Body', 'CRIT DMG', '64.8%', '/img13.webp', '2026-05-10T10:12:00Z'),

(3, 'Watchmaker Dream Machinations', 'Pocket Watch', 'Link Rope', 'Break Effect', '64.8%', '/img14.webp', '2026-05-10T10:13:00Z'),

(1, 'Longevous Disciple', 'Celestial Silk Robe', 'Body', 'HP%', '43.2%', '/img15.webp', '2026-05-10T10:14:00Z'),

(2, 'Belobog of the Architects', 'Fortress Sphere', 'Planar Sphere', 'DEF%', '54.0%', '/img16.webp', '2026-05-10T10:15:00Z'),

(3, 'Iron Cavalry Against the Scourge', 'Battle Greaves', 'Feet', 'SPD', '25', '/img17.webp', '2026-05-10T10:16:00Z'),

(1, 'Ever-Glorious Magical Girl', 'Contract Boots', 'Feet', 'HP%', '6.9%', '/img18.webp', '2026-05-10T10:17:00Z'),

(2, 'Diviner of Distant Reach', 'Ingenium Hand', 'Hand', 'ATK', '56', '/img19.webp', '2026-05-10T10:18:00Z'),

(3, 'Hero of Triumphant Song', 'Firechasing Shinguard', 'Feet', 'SPD', '25', '/img20.webp', '2026-05-10T10:19:00Z');

INSERT OR IGNORE INTO substat (
    relic_id, name, value, total_rolls, low_rolls, med_rolls, high_rolls
) VALUES
(1, 'ATK%', '3.8%', 1, 0, 1, 0),
(1, 'CRIT Rate', '2.9%', 1, 1, 0, 0),
(1, 'SPD', '2', 1, 1, 0, 0),
(1, 'Break Effect', '5.8%', 1, 0, 1, 0);